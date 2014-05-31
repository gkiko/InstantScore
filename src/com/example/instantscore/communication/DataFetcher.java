package com.example.instantscore.communication;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instantscore.R;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;

public class DataFetcher extends AsyncTask<String, Void, String>{
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
		View dialoglayout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) c.getCurrentFocus(), false);
		dialogLoad = new AlertDialog.Builder(c).setView(dialoglayout).show();
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
		dialogLoad.dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
	    builder.setMessage("error");
	    builder.setPositiveButton("OK", null);
	    builder.show();
		
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
