package com.example.instantscore.model;

/**
 * Created by gkiko on 6/5/14.
 */
public class EventContainer<T> {
    private T data;
    private int id;

    public EventContainer(T data, int id){
        this.data = data;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }
}
