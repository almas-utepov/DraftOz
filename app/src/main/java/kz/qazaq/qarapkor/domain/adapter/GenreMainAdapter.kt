package kz.qazaq.qarapkor.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import kz.qazaq.qarapkor.data.network.model.GenreResponse
import kz.qazaq.qarapkor.databinding.ItemFavoriteRcViewBinding
import kz.qazaq.qarapkor.databinding.ItemGenreRcViewBinding

class GenreMainAdapter(
    private val genreList: GenreResponse,
    private val context: Context,
    private val clickToChooseGenre: ItemOnClickChooseGenre) : RecyclerView.Adapter<GenreMainAdapter.GenreViewHolder>() {

    interface ItemOnClickChooseGenre {
        fun onClickToGenre(item: GenreResponse.GenreResponseItem)
    }

    inner class GenreViewHolder(private val binding: ItemGenreRcViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBingItemGenres(item: GenreResponse.GenreResponseItem){
            binding.textforGenres.text = item.name
            Glide.with(context)
                .load(item.link)
                .into(binding.imgGenre);
            itemView.setOnClickListener {
                clickToChooseGenre.onClickToGenre(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(ItemGenreRcViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        return holder.onBingItemGenres(genreList[position])
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
}