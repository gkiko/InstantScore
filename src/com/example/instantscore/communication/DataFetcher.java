package com.example.instantscore.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instantscore.MainSectionFragment;
import com.example.instantscore.R;
import com.example.instantscore.adapter.ListAdapter;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.example.instantscore.model.Game;
import com.example.instantscore.model.ListAdapterPriority;

public class DataFetcher extends AsyncTask<String, Void, MyChangeEvent>{
	private CopyOnWriteArrayList<CallbackListener> listeners;
	private AlertDialog dialogLoad;
	private AlertDialog dialogError;
	private Activity c;

	public DataFetcher(Activity c){
		listeners = new CopyOnWriteArrayList<CallbackListener>();
		this.c = c;
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
		LayoutInflater inflater = c.getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) c.getCurrentFocus(), false);
		dialogLoad = new AlertDialog.Builder(c).setView(dialogLayout).show();
	}

	@Override
	protected MyChangeEvent doInBackground(String... params) {
		String data, urlStr = params[0];
		try {
			data = HttpClient.getHttpClientDoGetResponse(urlStr, null);
            return new MyChangeEvent(data);
		} catch (Exception e) {
            return new MyChangeEvent(e);
		}
	}

	@Override
	protected void onPostExecute(MyChangeEvent event) {
        // TODO: check if async task stopped
        dialogLoad.dismiss();
        if((event.getError() != null) || (((String)event.getResult()).length() == 0)){
            fireExceptionEvent(event);
        }else {
            fireDataDownloadEvent(event);
        }
	}

    private void fireExceptionEvent(MyChangeEvent event){
        for (CallbackListener l : listeners) {
            l.onException(event);
        }
    }

	private void fireDataDownloadEvent(MyChangeEvent event) {
		for (CallbackListener l : listeners) {
			l.onUpdate(event);
		}
	}
}
