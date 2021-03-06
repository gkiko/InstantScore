package com.kuku.instantscore.communication;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kuku.instantscore.model.League;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by gkiko on 8/1/14.
 */
public class JsonRequest extends Request<List<League>> {
    private final Gson gson = new Gson();
    private final Response.Listener<List<League>> listener;

    public JsonRequest(String url, Response.Listener<List<League>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, new TypeToken<List<League>>() {}.getType()),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(List<League> leagues) {
        listener.onResponse(leagues);
    }

}
