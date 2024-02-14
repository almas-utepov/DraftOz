package kz.mobydev.draft.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.draft.R
import kz.mobydev.draft.data.network.model.MainPageModel
import kz.mobydev.draft.data.network.model.MoviesMain
import kz.mobydev.draft.databinding.ItemMainCategoryRcBinding
import kz.mobydev.draft.databinding.ItemMainMovieRcBinding



class MainPageCategoryAdapter(
    private val mainMoviePageList: List<MainPageModel.MainPageModelItem.Movy>,
    private val context: Context,
    private val onItemClickListener: ClickInterface
) :
    RecyclerView.Adapter<MainPageCategoryAdapter.MainPageCategoryViewHolder>() {

    interface ClickInterface {
        fun onItemClick(item: MainPageModel.MainPageModelItem.Movy)
    }

    inner class MainPageCategoryViewHolder(private val binding: ItemMainCategoryRcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: MainPageModel.MainPageModelItem.Movy) {

            binding.apply {
                textTVMovieCategoryTitle.text = item.name
                textTVMovieCategoryDescription.text = item.genres[0].name
                Glide.with(context)
                    .load(item.poster.link)
                    .into(imgMovieCategory);

                itemView.setOnClickListener {
                    onItemClickListener.onItemClick(item)
                }

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageCategoryViewHolder {
        var adapter = MainPageCategoryViewHolder(
            ItemMainCategoryRcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))


        return adapter
    }

    override fun getItemCount(): Int {
        return mainMoviePageList.size
    }

    override fun onBindViewHolder(holder: MainPageCategoryViewHolder, position: Int) {
        holder.bindItem(mainMoviePageList[position])
    }


}