package com.example.instantscore.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.instantscore.R;

public class LoadingDialog extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return ProgressDialog.show(getActivity(), null, getResources().getString(R.string.loading_hdr));
	}
	
}
