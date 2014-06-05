package com.example.instantscore.listener;

import com.example.instantscore.model.EventContainer;

public interface CallbackListener {
	void onUpdate(EventContainer evt);
    void onException(EventContainer evt);
}
