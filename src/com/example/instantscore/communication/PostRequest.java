package com.example.instantscore.communication;

import com.example.instantscore.volley.AuthFailureError;
import com.example.instantscore.volley.NetworkResponse;
import com.example.instantscore.volley.Request;
import com.example.instantscore.volley.Response;
import com.example.instantscore.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by gkiko on 8/1/14.
 */
public class PostRequest<T> extends Request<T> {
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    public PostRequest(int method, String url, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        return (Response<T>) Response.success(new String(networkResponse.data), HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}