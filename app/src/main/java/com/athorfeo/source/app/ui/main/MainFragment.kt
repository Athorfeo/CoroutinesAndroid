package com.athorfeo.source.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.athorfeo.source.R
import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentMainBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.findFieldAnnotation
import com.athorfeo.source.util.ui.DialogUtil
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class MainFragment: BaseFragment(),
    View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener,
    MainAdapter.SearchMovieItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentMainBinding
    val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = MainAdapter(this)
        init(adapter)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_search -> searchMovie()
            R.id.button_filter -> {
                viewModel.filter((binding.recycler.adapter as MainAdapter).currentList)
            }
            R.id.button_reset -> viewModel.reset()
        }
    }

    override fun onRefresh() {
        activity?.let{
            DialogUtil.bottomConfirm(
                it,
                arrayOf(getString(R.string.text_question_sure_reset)),
                {searchMovie()},
                {binding.swipeRefresh.isRefreshing = false}
            ).show()
        }
    }

    override fun onClickItem(action: Boolean, movieId: Int) {
        viewModel.updateQuantity(action, movieId).observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    val message =
                        when(it.code){
                            ResponseCode.INSERT_DATABASE -> { "Se ha agregado la cantidad correctamente." }
                            ResponseCode.DELETE_DATABASE -> { "Se ha eliminado la cantidad correctamente." }
                            else -> { getString(R.string.success_msg_database_query) }
                        }

                    showSuccess(message, positiveCallback = {}, negativeCallback = {})
                },
                {
                    viewModel.setError(it.code, it.message)
                }
            )
        })
    }

    private fun init(adapter: MainAdapter){
        binding.viewModel = viewModel
        binding.clickListener = this
        binding.swipeRefresh.setOnRefreshListener(this)

        binding.recycler.adapter = adapter
        binding.recycler.isNestedScrollingEnabled = false

        subcribeUi(adapter)

        val obj = SearchMoviesResponse(0, listOf(), 0, 0)
        val annotation : SerializedName? = obj.findFieldAnnotation("page")
        Timber.i(annotation?.value ?: "Vacio")
    }

    private fun subcribeUi(adapter: MainAdapter){
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
            setLoading(it)
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it.process()
            Timber.i("Error")
        })

        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            adapter.submitList(movies)
            //showSuccess("Este es un mensaje", positiveCallback = {}, negativeCallback = {})
        })
    }

    private fun searchMovie(){
        viewModel.searchMovies(binding.inputSearch.text.toString())
        /*viewModel.testCoroutines().observe(viewLifecycleOwner, Observer {
            Timber.i("${it.status}")
        })*/
    }
}