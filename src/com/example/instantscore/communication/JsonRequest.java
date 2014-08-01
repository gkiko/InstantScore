package com.example.instantscore.communication;

import com.example.instantscore.model.League;
import com.example.instantscore.volley.NetworkResponse;
import com.example.instantscore.volley.ParseError;
import com.example.instantscore.volley.Request;
import com.example.instantscore.volley.Response;
import com.example.instantscore.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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
