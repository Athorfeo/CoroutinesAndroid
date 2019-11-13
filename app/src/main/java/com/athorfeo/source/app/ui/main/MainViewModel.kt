package com.athorfeo.source.app.ui.main

import androidx.lifecycle.*
import com.athorfeo.source.api.SearchMovieRequest
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.app.viewmodel.BaseViewModel
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.error.ErrorCode
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Viewmodel del fragmento principal que maneja la lista de películas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

@OpenForTesting
class MainViewModel @Inject constructor(private val repository: MovieRepository): BaseViewModel(){
    private val _movies = MediatorLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val search = MutableLiveData<String>()
    private val searchMovie: LiveData<Resource<List<Movie>>> = Transformations.switchMap(search){ search ->
        repository.searchMovies(
            SearchMovieRequest(search, 1)
        )
    }

    private var filter = false
    private fun setFilter(boolean: Boolean){ filter = boolean }



    init {
        viewModelScope.launch{
            _movies.addSource(searchMovie){ resource ->
                resource.process(
                    {
                        resource.data?.let {
                            if(it.isNotEmpty()){
                                if (filter){
                                    filter(it)
                                }else{
                                    _movies.value = it
                                }
                            }else{
                                setError(resource.code, resource.message)
                            }
                        }
                    },
                    {
                        resource.data?.let{
                            if(it.isNotEmpty()){
                                _movies.value = it
                            }else{
                                setError(resource.code, resource.message)
                            }
                        }
                    }
                )
            }
        }
    }

    fun searchMovies(string: String) {
        search.value = string
        setFilter(false)
    }

    fun filter(sourceList: List<Movie>){
        viewModelScope.launch(Dispatchers.Default){
            //val list = _movies.value?.filter { it.quantity > 0 }
            val list = sourceList.filter { it.quantity > 0 }

            if(list.isNullOrEmpty()){
                postError(ErrorCode.DATA_EMPTY)
                reset()
            }else{
                _movies.postValue(list)
                setFilter(true)
            }
        }
    }

    fun reset(){
        viewModelScope.launch {
            searchMovie.value?.data?.let {
                _movies.value = it
                setFilter(false)
            }
        }
    }

    fun updateQuantity(action: Boolean, movieId: Int) =
        if(action){
            repository.addQuantity(movieId)
        }else{
            repository.removeQuantity(movieId)
        }


    fun testCoroutines() = repository.testCoroutine()
}