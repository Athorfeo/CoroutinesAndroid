package com.athorfeo.source.app.ui.main

import androidx.lifecycle.*
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.app.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MovieRepository):  BaseViewModel(){
    private val search = MutableLiveData<String>()

    private val _movies = MediatorLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        viewModelScope.launch{
            _movies.addSource(Transformations.switchMap(search){ search ->
                repository.searchMovies(search, 1)
            }){ resource ->
                _movies.removeSource(movies)
                resource.process(
                    {
                        resource.data?.let {
                            _movies.value =
                                if(it.isNotEmpty()){
                                    it
                                }else{
                                    setError(ErrorResource(resource.code,resource.message))
                                    listOf()
                                }
                        }
                    },
                    {
                        resource.data?.let{
                            _movies.value =
                                if(it.isNotEmpty()){
                                    it
                                }else{listOf()}
                        }
                    }
                )
            }
        }
    }

    fun searchMovies(string: String) {
        search.value = string
    }
}