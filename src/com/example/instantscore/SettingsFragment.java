package com.example.instantscore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instantscore.communication.DataSender;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    public static final String KEY_PHONE_NUM = "phonenum";
    public static final String KEY_SECURITY_CODE = "securitycode";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference button = (Preference) findPreference("securitycode");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout_code, null);
                EditText codeView = (EditText)v.findViewById(R.id.editText2);
                codeView.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(KEY_SECURITY_CODE, ""));

                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(v)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dialog dialog2 = Dialog.class.cast(dialogInterface);
                                String text = ((TextView)dialog2.findViewById(R.id.editText2)).getText().toString();

                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                editor.putString(KEY_SECURITY_CODE, text);
                                editor.commit();

                            }
                        }).setNeutralButton("Request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String phoneNum = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(KEY_PHONE_NUM, "");
                                makeCodeRequest(phoneNum);
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });

        updateSettings();
    }

    private void updateSettings() {
        updateSettingsSummary(R.string.prefs_number_summary, KEY_PHONE_NUM);
        updateSettingsSummary(R.string.prefs_security_code_summary, KEY_SECURITY_CODE);
    }

    private void updateSettingsSummary(int stringId, String prefKey) {
        String prefsSummary = getActivity().getResources().getString(stringId) + " " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(prefKey, "");

        Preference connectionPref = findPreference(prefKey);
        connectionPref.setSummary(prefsSummary);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSettings();
    }

    private void makeCodeRequest(String phoneNum) {
        DataSender sender = new DataSender(getResources().getString(R.string.url_sms), null, getActivity());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("type", "new_code"));
        pairs.add(new BasicNameValuePair("phone_num", phoneNum));
        sender.execute(pairs);
    }

}
