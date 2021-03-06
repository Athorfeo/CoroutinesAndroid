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
import com.athorfeo.source.databinding.ItemListMovieBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MainAdapter(private val listener: SearchMovieItemListener):
    ListAdapter<Movie, MainAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { movie ->
            with(holder) {
                itemView.tag = movie
                bind(movie, createOnClickListener(movie))
            }
        }
    }

    private fun createOnClickListener(item: Movie): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.button_add -> {
                    listener.onClickItem(true, item.id)
                }
                R.id.button_remove -> {
                    listener.onClickItem(false, item.id)
                }
            }
        }
    }

    class ViewHolder (private val binding: ItemListMovieBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie, listener: View.OnClickListener){
            with(binding) {
                clickListener = listener
                this.viewModel = MainAdapterViewModel(movie)
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            //Lifehack
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    interface SearchMovieItemListener{
        fun onClickItem(action: Boolean, movieId: Int)
    }
}
