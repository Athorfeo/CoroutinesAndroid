package com.athorfeo.source.app.ui.base.activity

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.athorfeo.source.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager

/**
 * Actividad base de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val viewModel: BaseActivityViewModel by viewModels { viewModelFactory }

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subcribeUi()
    }

    /**
     * Antes de que el contexto se defina se establece el idioma de acuerdo a las preferencias
     * compartidas
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    override fun attachBaseContext(base: Context?) {
        base?.updateLanguage()?.let{
            super.attachBaseContext(it)
        }?:run {
            super.attachBaseContext(this)
        }
    }

    //endregion

    //region Interfaces Impl
    override fun supportFragmentInjector() = dispatchingAndroidInjector
    //endregion

    //region Init
    /**
     * Observa todos los livedata del viewmodel y establece los parámetros iniciales
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    private fun subcribeUi(){
        viewModel.language.observe(this, Observer {
            recreate()
        })
    }
    //endregion

    //region Otros
    /**
     * Actualiza el idioma de la app de acuerdo a la configuración del sistema.
     * @author Juan Ortiz
     * @date 10/09/2019
     * @return Devuelve el contexto recibido con una nueva configuración de idioma
     * */
    private fun Context.updateLanguage() : Context? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = preferences.getString("language", resources.getStringArray(R.array.languages_value)[0])

        language?.let {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)

            return createConfigurationContext(config)
        }

        return null
    }
    //endregion
}

