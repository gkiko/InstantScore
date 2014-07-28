package com.example.instantscore.communication;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.instantscore.R;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class DataSender extends AsyncTask<List<NameValuePair>, Void, String> {
    private String url;
    private View v;
    private Context c;

    public DataSender(String url, View v, Context c) {
        this.url = url;
        this.v = v;
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.v != null) {
            this.v.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(List<NameValuePair>... params) {
        String response = null;
        String urlParameters = pairsToUrl(params[0]);
        try {
            response = HttpClient.doPost(url, urlParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(c, result, Toast.LENGTH_SHORT).show();
        if (this.v != null) {
            this.v.findViewById(R.id.progressBar3).setVisibility(View.GONE);
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
}
