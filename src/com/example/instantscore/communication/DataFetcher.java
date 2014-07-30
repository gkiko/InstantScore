package com.example.instantscore.communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instantscore.R;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.example.instantscore.model.EventContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataFetcher extends AsyncTask<EventContainer, Void, List<EventContainer>>{
	private CopyOnWriteArrayList<CallbackListener> listeners;
	private AlertDialog dialogLoad;
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
        View dialogLayout = inflater.inflate(R.layout.dialog_layout_loading, (ViewGroup) c.getCurrentFocus(), false);
        dialogLoad = new AlertDialog.Builder(c).setView(dialogLayout).show();
        System.out.println("onPreExecute");
    }

    @Override
	protected List<EventContainer> doInBackground(EventContainer... params) {
		String data, urlStr;
        EventContainer newCont;
        List<EventContainer> eventList =  new ArrayList<EventContainer>();
        System.out.println("doInBackground start");
        for(EventContainer container : params){
            urlStr = (String)container.getData();
            try {
                data = HttpClient.getHttpClientDoGetResponse(urlStr, null);
                System.out.println("doInBackground http");
                newCont = new EventContainer(new MyChangeEvent(data), container.getId());
            } catch (Exception e) {
                newCont = new EventContainer(new MyChangeEvent(e), container.getId());
                e.printStackTrace();
            }
            eventList.add(newCont);
        }
        return eventList;
    }

	@Override
	protected void onPostExecute(List<EventContainer> events) {
        MyChangeEvent event;

        if(isCancelled()) return;
        dialogLoad.dismiss();
        for(EventContainer eventContainer : events) {
            event = (MyChangeEvent)eventContainer.getData();
            if ((event.getError() != null)) {
                fireExceptionEvent(eventContainer);
            } else {
                fireDataDownloadEvent(eventContainer);
            }
        }
	}

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dialogLoad.dismiss();
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
