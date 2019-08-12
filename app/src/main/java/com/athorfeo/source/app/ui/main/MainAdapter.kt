package com.athorfeo.source.app.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.athorfeo.source.R
import com.athorfeo.source.app.model.Movie

class MainAdapter(private val listener: SearchMovieItemListener): ListAdapter<Movie, MainAdapter.ViewHolder>(
    DiffCallback()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_movie, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { movie ->
            with(holder) {
                itemView.tag = movie
                bind(createOnClickListener(movie.id), movie)
            }
        }
    }

    private fun createOnClickListener(movieId: Int): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.buttonAdd -> { listener.onClickItem(true, movieId) }
                R.id.buttonRemove -> { listener.onClickItem(false, movieId)}
            }
        }
    }

    class ViewHolder (private val binding: com.athorfeo.source.databinding.ItemListMovieBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(listener: View.OnClickListener, movie: Movie){
            with(binding) {
                clickListener = listener
                viewModel = MainAdapterViewModel(movie)
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    interface SearchMovieItemListener{
        fun onClickItem(isAdd: Boolean, movieId: Int)
    }
}