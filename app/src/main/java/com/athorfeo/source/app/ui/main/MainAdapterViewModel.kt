package com.athorfeo.source.app.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.athorfeo.source.app.model.Movie

class MainAdapterViewModel(movie: Movie?): ViewModel() {
    private val movie = checkNotNull(movie)

    val id = ObservableField(this.movie.id)
    val title = ObservableField(this.movie.title)
    val overview = ObservableField(this.movie.overview)
    val titleString = ObservableField(this.movie.id)
}