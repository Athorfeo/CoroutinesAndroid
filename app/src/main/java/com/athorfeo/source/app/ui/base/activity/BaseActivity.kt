package com.athorfeo.source.app.ui.base.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.preference.PreferenceManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.athorfeo.source.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: BaseActivityViewModel by viewModels { viewModelFactory }

    override fun attachBaseContext(base: Context?) {
        base?.let{context ->
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val language = preferences.getString("language", context.resources.getStringArray(R.array.languages_value)[0])

            language?.let {
                super.attachBaseContext(updateResources(context, it))
            }
        }?:run { super.attachBaseContext(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subcribeUi()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun updateResources(context: Context, language: String) : Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    private fun subcribeUi(){
        viewModel.language.observe(this, Observer {
            recreate()
        })
    }
}

