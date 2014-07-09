package com.example.instantscore.communication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public class DataSender extends AsyncTask<List<NameValuePair>, Void, Void> {
	private String url;

	public DataSender(String url){
		this.url = url;
	}
	
	@Override
	protected Void doInBackground(List<NameValuePair>... params) {
		pairsToUrl(params[0]);
		String urlParameters = pairsToUrl(params[0]);
		try {
			HttpClient.doPost(url, urlParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
	
	private String pairsToUrl(List<NameValuePair> pairs){
		StringBuilder sb = new StringBuilder();
		for(NameValuePair pair : pairs){
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
}
