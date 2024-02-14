package kz.mobydev.draft.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import kz.mobydev.draft.data.network.model.MoviesMain
import kz.mobydev.draft.databinding.ItemMainMovieRcBinding

class MainMovieAdapter(private val mainMovieList: MoviesMain, private val context: Context, private val itemMovieClick:ItemClick) :
    RecyclerView.Adapter<MainMovieAdapter.MainMovieViewHolder>()  {

    interface ItemClick{
        fun onItemClickMainMovie(item:MoviesMain.MoviesMainItem)
    }
    inner class MainMovieViewHolder(private val binding: ItemMainMovieRcBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(mainMovieItem: MoviesMain.MoviesMainItem) {

           binding.apply {
               textTvMainMovieTittle.text = mainMovieItem.movie.name
               textTvMainMovieDescription.text = mainMovieItem.movie.description

               Glide.with(context)
                   .load(mainMovieItem.link)
                   .into(imgMainMovie);

               itemView.setOnClickListener {
                   itemMovieClick.onItemClickMainMovie(mainMovieItem)
               }

           }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMovieViewHolder {
        return MainMovieViewHolder(
            ItemMainMovieRcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mainMovieList.size
    }

    override fun onBindViewHolder(holder: MainMovieViewHolder, position: Int) {
        holder.bindItem(mainMovieList[position])
    }


}