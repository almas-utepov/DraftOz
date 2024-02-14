package kz.mobydev.draft.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.draft.data.network.model.SearchResponseModel
import kz.mobydev.draft.databinding.ItemFavoriteRcViewBinding

class SearchMovieAdapter(
    private val response: SearchResponseModel,
    private val context: Context,
    private val onItemSearchMovieClick: SearchClickMovie
) : RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {
    interface SearchClickMovie {
        fun onItemClick(item: SearchResponseModel.SearchResponseModelItem)
    }


    inner class SearchMovieViewHolder(private val binding: ItemFavoriteRcViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindSearchResponseMovie(item: SearchResponseModel.SearchResponseModelItem) {

            binding.apply {

                textTvTitle.text = item.name
                textTvYear.text = item.year.toString()

                var additionalInfoGenre = " "
                for (i in item.genres){
                    additionalInfoGenre += "• ${i.name}"
                }
                Glide.with(context)
                    .load(item.poster.link)
                    .into(imgFavorite);
                btnWatchVideo.setOnClickListener {
                    onItemSearchMovieClick.onItemClick(item)
                }
                itemView.setOnClickListener {
                    onItemSearchMovieClick.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        return SearchMovieViewHolder(
            ItemFavoriteRcViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        holder.bindSearchResponseMovie(response[position])
    }

}