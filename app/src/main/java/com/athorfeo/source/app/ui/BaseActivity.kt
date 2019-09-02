package com.athorfeo.source.app.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.*
import com.athorfeo.source.R
import com.athorfeo.source.databinding.ActivityMainBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun attachBaseContext(base: Context?) {
        base?.let{context ->
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val language = preferences.getString("language", context.resources.getStringArray(R.array.languages_value)[0])

            language?.let {
                super.attachBaseContext(updateResources(context, it))
            }
        }?:run { super.attachBaseContext(this) }

    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun updateResources(context: Context, language: String) : Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

}

