package com.example.instantscore.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.test2.MainActivity;
import com.example.test2.R;

public class NumberDialog extends DialogFragment {
	public static NumberDialog newInstance() {
		NumberDialog frag = new NumberDialog();
        return frag;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.number_dialog_layout, null);
        final EditText numberEditText = (EditText) view.findViewById(R.id.number_editText);
		
		Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle(R.string.number_dialog_hdr)
		.setPositiveButton(R.string.number_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((MainActivity)getActivity()).doPositiveClick(numberEditText.getText().toString());
			}
		})
		.setNegativeButton(R.string.number_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((MainActivity)getActivity()).doNegativeClick();
			}
		});
		return alertDialogBuilder.create();
	}
	
}
