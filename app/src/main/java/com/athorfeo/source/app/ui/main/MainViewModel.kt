package com.athorfeo.source.app.ui.main

import androidx.lifecycle.*
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.app.viewmodel.BaseViewModel
import com.athorfeo.source.utility.ErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Viewmodel del fragmento principal que maneja la lista de películas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MainViewModel @Inject constructor(private val repository: MovieRepository): BaseViewModel(){
    private val search = MutableLiveData<String>()
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
                                    setError(resource.code, resource.message)
                                    listOf()
                                }
                        }
                    },
                    {
                        resource.data?.let{
                            _movies.value =
                                if(it.isNotEmpty()){
                                    it
                                }else{
                                    setError(resource.code, resource.message)
                                    listOf()
                                }
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
        viewModelScope.launch(Dispatchers.Default){
            val list = _movies.value?.filter { it.quantity > 0 }
            if(list.isNullOrEmpty()){
                postError(ErrorCode.DATA_EMPTY)
                reset()
            }else{
                _movies.postValue(list)
            }
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