package com.athorfeo.source.app.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.athorfeo.source.R
import com.athorfeo.source.di.Injectable

class SettingsFragment: PreferenceFragmentCompat(), Injectable {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}