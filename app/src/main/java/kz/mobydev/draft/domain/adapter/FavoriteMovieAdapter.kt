package kz.mobydev.draft.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.draft.data.network.model.FavoriteModel
import kz.mobydev.draft.data.network.model.MainPageModel
import kz.mobydev.draft.databinding.ItemFavoriteRcViewBinding
import kz.mobydev.draft.databinding.ItemMainCategoryRcBinding

class FavoriteMovieAdapter(
    private val favoriteModelList: FavoriteModel,
    private val context: Context,
    private val onItemFavoriteMovieClick: FavoriteClickMovie
    ):RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {
    interface FavoriteClickMovie {
        fun onItemClick(item:FavoriteModel.FavoriteModelItem)
    }

    inner class FavoriteMovieViewHolder(private val binding: ItemFavoriteRcViewBinding):RecyclerView.ViewHolder(binding.root){
        fun bindFavoriteItem(item:FavoriteModel.FavoriteModelItem){

            binding.apply {
                textTvTitle.text = item.name
                textTvYear.text = item.year.toString()
                var additionalInfoGenre = " "
                for (i in item.genres){
                    additionalInfoGenre += "• ${i.name}"
                }
                textTvGenres.text = additionalInfoGenre
                Glide.with(context)
                    .load(item.poster.link)
                    .into(imgFavorite);
                btnWatchVideo.setOnClickListener {
                    onItemFavoriteMovieClick.onItemClick(item)
                }
                itemView.setOnClickListener {
                    onItemFavoriteMovieClick.onItemClick(item)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
       return FavoriteMovieViewHolder(ItemFavoriteRcViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return favoriteModelList.size
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
       holder.bindFavoriteItem(favoriteModelList[position])
    }
}