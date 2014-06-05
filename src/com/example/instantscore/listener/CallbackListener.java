package com.example.instantscore.listener;

public interface CallbackListener {
	void onUpdate(MyChangeEvent evt);
    void onException(MyChangeEvent evt);
}
