package com.kuku.instantscore;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends DialogFragment {

    public AboutFragment(){
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_layout, container);
        getDialog().setTitle("About");

        TextView versionText = (TextView)view.findViewById(R.id.dialog_version_number);
        PackageInfo pinfo = null;
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText(getResources().getText(R.string.about_version) + pinfo.versionName /*BuildConfig.VERSION_NAME*/);

        TextView feedback = (TextView) view.findViewById(R.id.send_feedback_mail);
        makeTextViewHyperlink(feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{activity.getResources().getText(R.string.mail_address).toString()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, activity.getResources().getText(R.string.mail_subject).toString());

                activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        return view;
    }

    public static void makeTextViewHyperlink(TextView tv) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tv.getText());
        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}
