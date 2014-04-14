package com.example.instantscore.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.test2.R;

public class LoadingDialog extends DialogFragment {
	
	public static LoadingDialog newInstance() {
		LoadingDialog frag = new LoadingDialog ();
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return ProgressDialog.show(getActivity(), null, getResources().getString(R.string.loading_hdr));
	}
	
}
