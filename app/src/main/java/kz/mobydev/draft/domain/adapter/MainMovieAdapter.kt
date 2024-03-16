package kz.qazaq.qarapkor.domain.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import kz.qazaq.qarapkor.data.network.model.CategoryMovieResponse
import kz.qazaq.qarapkor.data.network.model.MoviesMain
import kz.qazaq.qarapkor.databinding.ItemMainMovieRcBinding
import java.text.SimpleDateFormat

class MainMovieAdapter(private val mainMovieList: List<CategoryMovieResponse.Content>, private val context: Context, private val itemMovieClick:ItemClick) :
    RecyclerView.Adapter<MainMovieAdapter.MainMovieViewHolder>()  {

    interface ItemClick{
        fun onItemClickMainMovie(item:CategoryMovieResponse.Content)
    }
    inner class MainMovieViewHolder(private val binding: ItemMainMovieRcBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(mainMovieItem:CategoryMovieResponse.Content) {

           binding.apply {
               textTvMainMovieTittle.text = mainMovieItem.name
               textTvMainMovieDescription.text =  mainMovieItem.description
               val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

// Разбираем строку в объект Date
               val date = format.parse(mainMovieItem.createdDate)

// Создаем другой объект SimpleDateFormat для форматирования года
               val yearFormat = SimpleDateFormat("yyyy")

// Получаем год как строку
               val year = yearFormat.format(date)
               textTvImgMainMovieAbout.text = year + " жыл"

               Glide.with(context)
                   .load(mainMovieItem.poster.link)
                   .into(imgMainMovieRC);

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