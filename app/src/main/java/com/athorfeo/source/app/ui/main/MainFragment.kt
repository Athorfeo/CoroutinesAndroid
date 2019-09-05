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
import com.athorfeo.source.utility.findAnnotation
import com.athorfeo.source.utility.ui.DialogUtil
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

class MainFragment: BaseFragment(),
    View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener,
    MainAdapter.SearchMovieItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

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
            R.id.button_filter -> viewModel.filter()
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
        if(action){
            //updateShoppingCart(ShoppingCartEntity(movieId, 1))
        }else{
            //updateShoppingCart(ShoppingCartEntity(movieId, 1), false)
        }
    }

    private fun init(adapter: MainAdapter){
        binding.viewModel = viewModel
        binding.clickListener = this
        binding.swipeRefresh.setOnRefreshListener(this)

        binding.recycler.adapter = adapter
        binding.recycler.isNestedScrollingEnabled = false

        subcribeUi(adapter)

        val obj = SearchMoviesResponse(0, listOf(), 0, 0)
        val annotation : SerializedName? = obj.findAnnotation("page")
        Log.i("Annotation", annotation?.value ?: "Vacio")
    }

    private fun subcribeUi(adapter: MainAdapter){
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
            setLoading(it)
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it.process()
        })

        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            adapter.submitList(movies)
        })
    }

    private fun searchMovie(){
        viewModel.searchMovies(binding.inputSearch.text.toString())
    }
}