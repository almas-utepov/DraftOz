package kz.mobydev.draft.presentation.ui.fragmentMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.mobydev.draft.R
import kz.mobydev.draft.data.network.api.ApiInterface
import kz.mobydev.draft.data.network.api.ServiceBuilder
import kz.mobydev.draft.data.network.model.FavoriteModel
import kz.mobydev.draft.data.network.model.MovieIdModel
import kz.mobydev.draft.data.preferences.PreferenceProvider
import kz.mobydev.draft.databinding.FragmentFavoriteBinding
import kz.mobydev.draft.domain.Utils.CustomDividerItemDecoration
import kz.mobydev.draft.domain.adapter.FavoriteMovieAdapter
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost

class FavoriteFragment : Fragment() {
    private var binding: FragmentFavoriteBinding? = null
    private val vm: VM by activityViewModels()




    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(false, btnExitVisible = false, titleVisible = true, title = "Тізім")
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(false, btnExitVisible = false, titleVisible = true, title = "Тізім")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = PreferenceProvider(requireContext()).getToken()
        lifecycleScope.launch{
            retrofitStart(token!!)
        }
    }


    suspend fun retrofitStart(token:String){
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        val data  = runCatching {
            response.getFavoriteMovies("Bearer $token")
        }.onFailure {  error ->

        }.getOrNull()

        if (data != null && data.isNotEmpty()) {

            val adapter = FavoriteMovieAdapter(
                data,
                requireContext().applicationContext,
                object : FavoriteMovieAdapter.FavoriteClickMovie {
                    override fun onItemClick(item: FavoriteModel.FavoriteModelItem) {
                        vm.movieIdForAbout.value = MovieIdModel(item.id)
                        findNavController().navigate(R.id.aboutMovieFragment)
                    }


                })
            binding?.rcFavoriteFragment?.adapter = adapter
            binding?.rcFavoriteFragment?.addItemDecoration(
                    CustomDividerItemDecoration(
                        requireContext().getDrawable(R.drawable.divider_1dp_grey)!!
                    )
            )


        }
    }

}