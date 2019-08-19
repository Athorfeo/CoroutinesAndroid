package com.athorfeo.source.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.athorfeo.source.R
import com.athorfeo.source.app.model.Session
import com.athorfeo.source.app.model.save
import com.athorfeo.source.app.ui.BaseFragment
import com.athorfeo.source.databinding.FragmentMainBinding
import com.athorfeo.source.utility.constant.Constants
import javax.inject.Inject

class MainFragment: BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    MainAdapter.SearchMovieItemListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //binding = FragmentMainBinding.inflate(inflater, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter(this)
        init(adapter)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSearch -> {searchMovie()}
            R.id.buttonFilter -> {viewModel.filter()}
            R.id.buttonReset -> {viewModel.reset()}
        }
    }

    override fun onRefresh() {
        searchMovie()
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
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.isNestedScrollingEnabled = false

        subcribeUi(adapter)
    }

    private fun subcribeUi(adapter: MainAdapter){
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeRefreshLayout.isRefreshing = it
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it.process()
        })

        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            adapter.submitList(movies)
        })
    }

    private fun searchMovie(){
        viewModel.searchMovies(binding.editTextSearch.text.toString())
    }
}