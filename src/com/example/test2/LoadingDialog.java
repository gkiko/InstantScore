package com.example.test2;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingDialog extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.loading_hdr));
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
}
