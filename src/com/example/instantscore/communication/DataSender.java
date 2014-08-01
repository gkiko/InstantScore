package com.example.instantscore.communication;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.example.instantscore.model.EventContainer;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataSender extends AsyncTask<EventContainer, Void, EventContainer> {
    private CopyOnWriteArrayList<CallbackListener> listeners;
    private String url;
    private View progressBar;
    private Context c;

    public DataSender(String url, View v, Context c) {
        this.url = url;
        this.progressBar = v;
        this.c = c;
        listeners = new CopyOnWriteArrayList<CallbackListener>();
    }

    public void addMyChangeListener(CallbackListener l) {
        this.listeners.add(l);
    }

    public void removeMyChangeListener(CallbackListener l) {
        this.listeners.remove(l);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected EventContainer doInBackground(EventContainer... containers) {
        String response = null;
        EventContainer newCont;
        List<NameValuePair> params = (List<NameValuePair>) containers[0].getData();
        String urlParameters = pairsToUrl(params);
        try {
            response = HttpClient.doPost(url, urlParameters);
            newCont = new EventContainer(new MyChangeEvent(response), containers[0].getId());
        } catch (Exception e) {
            e.printStackTrace();
            newCont = new EventContainer(new MyChangeEvent(e), containers[0].getId());
        }
        return newCont;
    }

    @Override
    protected void onPostExecute(EventContainer result) {
        super.onPostExecute(result);
        MyChangeEvent evt = (MyChangeEvent) result.getData();
        Toast.makeText(c, (String)evt.getResult(), Toast.LENGTH_LONG).show();
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }
        if ((evt.getError() != null)) {
            fireExceptionEvent(result);
        } else {
            fireDataDownloadEvent(result);
        }
    }

    private String pairsToUrl(List<NameValuePair> pairs) {
        StringBuilder sb = new StringBuilder();
        for (NameValuePair pair : pairs) {
            sb.append(pair.getName());
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void fireExceptionEvent(EventContainer event){
        for (CallbackListener l : listeners) {
            l.onException(event);
        }
    }

    private void fireDataDownloadEvent(EventContainer event) {
        for (CallbackListener l : listeners) {
            l.onUpdate(event);
        }
    }
}
