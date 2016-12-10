package com.example.Project.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.Project.R;

import java.util.List;

public class SimplePreferenceActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @SuppressLint("Override")
    protected boolean isValidFragment(String fragmentName) {

        return SimplePreferenceFragment.class.getName().equals(fragmentName);
    }


    public static class SimplePreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.simple_prefs);
        }
    }

}


