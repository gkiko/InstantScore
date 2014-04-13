package com.example.test2;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	public static final String KEY_PHONE_NUM = "phonenum";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		updatePrefsSummary();
	}
	
	private void updatePrefsSummary(){
		updateNumberSummary();
	}
	
	private void updateNumberSummary(){
		String numberPrefsSummary = getActivity().getResources().getString(R.string.prefs_number_summary) + " " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(KEY_PHONE_NUM, "");
		
		Preference connectionPref = findPreference(KEY_PHONE_NUM);
		connectionPref.setSummary(numberPrefsSummary);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(KEY_PHONE_NUM)) {
			updateNumberSummary();
        }
	}

}
