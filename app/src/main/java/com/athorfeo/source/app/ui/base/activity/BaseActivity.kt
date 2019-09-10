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

/**
 * Actividad base de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    protected val viewModel: BaseActivityViewModel by viewModels { viewModelFactory }

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

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
        base?.let{context ->
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val language = preferences.getString("language", context.resources.getStringArray(R.array.languages_value)[0])

            language?.let {
                super.attachBaseContext(updateLocale(context, it))
            }
        }?:run { super.attachBaseContext(this) }
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
     * Observa todos los livedata de viewmodel
     * @author Juan Ortiz
     * @date 10/09/2019
     * @param context Contexto base
     * @param language Lenguaje seleccionado en formato locale (en, es, etc)
     * @return Devuelve el contexto recibido con una nueva configuración de idioma
     * */
    private fun updateLocale(context: Context, language: String) : Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    //endregion
}

