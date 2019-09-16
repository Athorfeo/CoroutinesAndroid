package com.athorfeo.source.app.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.athorfeo.source.app.model.Movie

/**
 * Viewmodel del adaptador de lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MainAdapterViewModel(movie: Movie?): ViewModel() {
    private val movie = checkNotNull(movie)

    val id = ObservableField(this.movie.id)
    val title = ObservableField(this.movie.title)
    val quantity = ObservableField(this.movie.quantity)
}