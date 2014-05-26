package com.example.instantscore.communication;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;

import com.example.instantscore.dialog.LoadingDialog;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;

public class DataFetcher extends AsyncTask<String, Void, String>{
	private CopyOnWriteArrayList<CallbackListener> listeners;
	private android.support.v4.app.FragmentManager fragmentManager;
	private LoadingDialog dialog;
	
	public DataFetcher(android.support.v4.app.FragmentManager fragmentManager){
		listeners = new CopyOnWriteArrayList<CallbackListener>();
		this.fragmentManager = fragmentManager;
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
		dialog = new LoadingDialog();
		dialog.show(fragmentManager, "");
	}

	@Override
	protected String doInBackground(String... params) {
		String data = null, urlStr = params[0];
		try {
			data = HttpClient.getHttpClientDoGetResponse(urlStr, null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@Override
	protected void onPostExecute(String result) {
		dialog.dismiss();
		if(result!=null)
			fireDataDownloadEvent(result);
	}
	
	private void fireDataDownloadEvent(String data) {
		MyChangeEvent evt = new MyChangeEvent(data);

		for (CallbackListener l : listeners) {
			l.onUpdate(evt);
		}
	}
	
}
