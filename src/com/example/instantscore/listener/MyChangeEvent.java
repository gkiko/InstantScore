package com.example.instantscore.listener;

import java.util.EventObject;

/**
 * Class is used to pass object as event
 *
 * @author user
 */
public class MyChangeEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private Object result;
    private Exception error;

    public MyChangeEvent(Object result) {
        super(result);
        this.result = result;
    }

    public MyChangeEvent(Exception error) {
        super(error);
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

}
