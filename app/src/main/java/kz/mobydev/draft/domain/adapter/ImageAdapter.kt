package kz.mobydev.draft.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.draft.data.network.model.MovieInfoResponse
import kz.mobydev.draft.databinding.ItemImgRcBinding

class ImageAdapter(
    private val imageUrl: List<MovieInfoResponse.Screenshot>,
    private val context: Context,
    private val clickListener: ImageClick
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface ImageClick {
        fun onImgClick(item: MovieInfoResponse.Screenshot)
    }

    inner class ImageViewHolder(private val binding: ItemImgRcBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBingImg(item: MovieInfoResponse.Screenshot) {
            binding.run {
                Glide.with(context)
                    .load(item.link)
                    .into(binding.img120);
            }
            itemView.setOnClickListener {
                clickListener.onImgClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImgRcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageUrl.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        return holder.onBingImg(imageUrl[position])
    }
}