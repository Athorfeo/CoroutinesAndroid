package com.athorfeo.source.app.ui.main

import androidx.lifecycle.*
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.app.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MovieRepository):  BaseViewModel(){
    private val search = MutableLiveData<String>()

    /* Resources */
    private val searchMovie: LiveData<Resource<List<Movie>>> = Transformations.switchMap(search){ search -> repository.searchMovies(search, 1) }

    private val _movies = MediatorLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        viewModelScope.launch{
            _movies.addSource(searchMovie){ resource ->
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

    fun filter(){
        /*val liveData = liveData(Dispatchers.Default){
            val list = searchMovie.value?.data?.filter { it.quantity > 0 }
            emit(list)
        }

        viewModelScope.launch{
            _movies.addSource(liveData){
                _movies.removeSource(liveData)
                _movies.value = it
            }
        }*/

        viewModelScope.launch(Dispatchers.Default){
            val list = searchMovie.value?.data?.filter { it.quantity > 0 }
            _movies.postValue(list)
        }
    }

    fun reset(){
        viewModelScope.launch {
            searchMovie.value?.data?.let {
                _movies.value = it
            }
        }
    }
}