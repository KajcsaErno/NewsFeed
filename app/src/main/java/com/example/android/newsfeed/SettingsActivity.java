package com.example.android.newsfeed;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Update to the current value for a user friendly UI
            Preference sectionCategory = findPreference(getString(R.string.settings_category_key));
            bindPreferenceSummaryToValue(sectionCategory);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferenceSummaryToValue(pageSize);

            Preference search = findPreference(getString(R.string.settings_search_key));
            bindPreferenceSummaryToValue(search);

            Preference fromDate = findPreference(getString(R.string.settings_from_date_key));
            bindPreferenceSummaryToValue(fromDate);
        }

        @Override
        // The code in this method takes care of updating the displayed preference summary after it has been changed
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof MultiSelectListPreference) {
                // For multi select list preferences we should show a list of the selected options
                MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
                CharSequence[] values = multiSelectListPreference.getEntries();
                StringBuilder option = new StringBuilder();
                for (String categories : (Set<String>) newValue) {
                    int index = multiSelectListPreference.findIndexOfValue(categories);
                    if (index >= 0) {
                        if (option.length() != 0) {
                            option.append(", ");
                        }
                        option.append(values[index]);
                    }
                }
                preference.setSummary(option);

            } else if (preference instanceof ListPreference) {
                // // For a single select list preferences we should show a the selected options
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // For all other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }


        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(this);

            // Trigger the listener immediately with the preference's current value.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            Object value;
            if (preference instanceof MultiSelectListPreference) {
                value = sharedPreferences.getStringSet(preference.getKey(), new HashSet<String>());
            } else {
                value = sharedPreferences.getString(preference.getKey(), "");
            }
            onPreferenceChange(preference, value);
        }
    }
}