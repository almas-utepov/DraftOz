package kz.qazaq.qarapkor.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.qazaq.qarapkor.data.network.model.FavoriteModel
import kz.qazaq.qarapkor.data.network.model.SeriesModel
import kz.qazaq.qarapkor.databinding.ItemSeriesRcViewBinding

class SeriesMovieAdapter(
    private val seriesModelVideo:List<SeriesModel.SeriesModelItem.Video>,
    private val context: Context,
    private val onItemClick:SeriesClickMovie):RecyclerView.Adapter<SeriesMovieAdapter.SeriesMovieViewHolder>() {

    interface SeriesClickMovie {
        fun onItemClick(item: SeriesModel.SeriesModelItem.Video)
    }
    inner class SeriesMovieViewHolder(private val binding:ItemSeriesRcViewBinding):RecyclerView.ViewHolder(binding.root){
        fun bindSeriesItem(item: SeriesModel.SeriesModelItem.Video){
            binding.textSeries.text = "${item.number}-ші бөлім"

            //https://youtu.be/hycgdjf54i8
            //http://img.youtube.com/vi/hycgdjf54i8/maxresdefault.jpg
            Glide.with(context)
                .load("http://img.youtube.com/vi/${item.link}/maxresdefault.jpg")
                .into(binding.imgTvSeries)
            itemView.setOnClickListener {
                onItemClick.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesMovieViewHolder {
        return SeriesMovieViewHolder(ItemSeriesRcViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return  seriesModelVideo.size
    }

    override fun onBindViewHolder(holder: SeriesMovieViewHolder, position: Int) {
        holder.bindSeriesItem(seriesModelVideo[position] )
    }


}