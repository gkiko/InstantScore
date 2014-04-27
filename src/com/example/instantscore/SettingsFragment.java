package com.example.instantscore;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.instantscore.communication.DataSender;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	public static final String KEY_PHONE_NUM = "phonenum";
	public static final String KEY_SECURITY_CODE = "securitycode";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		updateSettings();
	}
	
	private void updateSettings(){
		updateSettingsSummary(R.string.prefs_number_summary, KEY_PHONE_NUM);
		updateSettingsSummary(R.string.prefs_security_code_summary, KEY_SECURITY_CODE);
	}
	
	private void updateSettingsSummary(int stringId, String prefKey){
		String numberPrefsSummary = getActivity().getResources().getString(stringId) + " " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(prefKey, "");
		
		Preference connectionPref = findPreference(prefKey);
		connectionPref.setSummary(numberPrefsSummary);
	}
	

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		updateSettings();
		if(key.equals(KEY_PHONE_NUM)){
			makeCodeRequest(sharedPreferences, KEY_PHONE_NUM);
		}
	}

	private void makeCodeRequest(SharedPreferences sharedPreferences, String key) {
		DataSender sender = new DataSender();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(key, sharedPreferences.getString(key, "")));
		sender.execute(pairs);
	}

}
