package com.athorfeo.source.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.BaseFragment
import com.athorfeo.source.databinding.FragmentMainBinding
import com.athorfeo.source.utility.ui.DialogUtil
import javax.inject.Inject

class MainFragment: BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
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
            R.id.buttonSearch -> {searchMovie()}
            R.id.buttonFilter -> {viewModel.filter()}
            R.id.buttonReset -> {viewModel.reset()}
        }
    }

    override fun onRefresh() {
        activity?.let{
            DialogUtil.bottomConfirm(
                it,
                arrayOf(getString(R.string.text_question_sure_reset)),
                {searchMovie()},
                {binding.swipeRefreshLayout.isRefreshing = false}
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