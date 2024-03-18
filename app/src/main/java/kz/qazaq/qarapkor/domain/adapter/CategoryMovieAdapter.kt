package kz.qazaq.qarapkor.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.qazaq.qarapkor.data.network.model.CategoryMovieResponse
import kz.qazaq.qarapkor.data.network.model.FavoriteModel
import kz.qazaq.qarapkor.databinding.ItemFavoriteRcViewBinding

class CategoryMovieAdapter(private val categoryResponse: CategoryMovieResponse,
                           private val context: Context,
                           private val onItemCategoryMovieClick: CategoryClickMovie
): RecyclerView.Adapter<CategoryMovieAdapter.CategoryMovieViewHolder>() {
    interface CategoryClickMovie {
        fun onItemClick(item: CategoryMovieResponse.Content)
    }

    inner class CategoryMovieViewHolder(private val binding: ItemFavoriteRcViewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bindCategoryMovieItem(item: CategoryMovieResponse.Content){

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
                    onItemCategoryMovieClick.onItemClick(item)
                }
                itemView.setOnClickListener {
                    onItemCategoryMovieClick.onItemClick(item)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMovieViewHolder {
        return CategoryMovieViewHolder(ItemFavoriteRcViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryResponse.content.size
    }

    override fun onBindViewHolder(holder: CategoryMovieViewHolder, position: Int) {
        holder.bindCategoryMovieItem(categoryResponse.content[position])
    }
}