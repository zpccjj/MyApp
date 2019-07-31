package com.hsic.fragment;

import com.hsic.qp.szjc.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class FragmentConfig extends PreferenceFragment {
    private SharedPreferences mPreferences;


    private Context getContext(){
        return getActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.config);

        mPreferences = getPreferenceScreen().getSharedPreferences();

        findPreference("DeviceID").setSummary(mPreferences.getString("DeviceID", ""));

        findPreference("webServer").setSummary(mPreferences.getString("webServer", getActivity().getResources().getString(R.string.config_ws)));

        findPreference("overTime").setSummary(mPreferences.getString("overTime", getActivity().getResources().getString(R.string.config_time)));

        findPreference("power_w").setSummary(mPreferences.getString("power_w", getActivity().getResources().getString(R.string.config_power_w)));


        String verString = "";
        try {
            PackageInfo packageinfo = getActivity().getPackageManager()
                    .getPackageInfo("com.hsic.qp.szjc", 0);
            verString = "V " + packageinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        findPreference("version").setSummary(verString);
    }


//	@Override
//	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
//			Preference preference) {
//		// TODO Auto-generated method stub
//
//		return super.onPreferenceTreeClick(preferenceScreen, preference);
//	}

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(
                        mOnSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(
                        mOnSharedPreferenceChangeListener);
    }

    private OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(
                SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);
            if (pref instanceof EditTextPreference) {
                EditTextPreference etp = (EditTextPreference) pref;
                pref.setSummary(etp.getText());
            }
        }
    };

}
