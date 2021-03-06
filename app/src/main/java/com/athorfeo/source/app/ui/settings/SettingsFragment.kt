package com.athorfeo.source.app.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.activity.BaseActivityViewModel
import com.athorfeo.source.di.Injectable
import javax.inject.Inject

/**
 * Fragmento que maneja las preferencias del sistema
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class SettingsFragment: PreferenceFragmentCompat(), Injectable, SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BaseActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[BaseActivityViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key){
            getString(R.string.preference_key_language) -> {
                viewModel.language.value = sharedPreferences.getString(key, "en")
            }
        }
    }
}