package com.athorfeo.source.app.ui.settings

import android.os.Bundle
import android.view.Menu
import androidx.preference.PreferenceFragmentCompat
import com.athorfeo.source.R
import com.athorfeo.source.di.Injectable

class SettingsFragment: PreferenceFragmentCompat(), Injectable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

}