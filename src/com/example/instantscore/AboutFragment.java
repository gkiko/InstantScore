package com.example.instantscore;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gkiko on 11/16/14.
 */
public class AboutFragment extends DialogFragment {

    public AboutFragment(){
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_layout, container);
        getDialog().setTitle("About");

        return view;
    }
}
