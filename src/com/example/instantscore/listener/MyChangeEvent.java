package com.example.instantscore.listener;

import java.util.EventObject;

/**
 * Class is used to pass object as event
 * @author user
 *
 */
public class MyChangeEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	public Object source;
	
	public MyChangeEvent(Object source) {
		super(source);
		this.source = source;
	}

}
