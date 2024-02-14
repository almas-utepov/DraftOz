package kz.mobydev.draft.presentation.ui.fragmentAdditional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.mobydev.draft.R
import kz.mobydev.draft.data.network.api.ApiInterface
import kz.mobydev.draft.data.network.api.ServiceBuilder
import kz.mobydev.draft.data.network.model.LinkVideo
import kz.mobydev.draft.data.network.model.SeriesModel
import kz.mobydev.draft.data.preferences.PreferenceProvider
import kz.mobydev.draft.databinding.FragmentSeriesBinding
import kz.mobydev.draft.domain.Utils.CustomDividerItemDecoration
import kz.mobydev.draft.domain.adapter.SeriesMovieAdapter
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost

class SeriesFragment : Fragment() {
    private var binding:FragmentSeriesBinding? = null
    private val vm: VM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Бөлімдер")
            onClickListener(R.id.action_seriesFragment_to_aboutMovieFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Бөлімдер")
            onClickListener(R.id.action_seriesFragment_to_aboutMovieFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Бөлімдер")
            onClickListener(R.id.action_seriesFragment_to_aboutMovieFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeriesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_seriesFragment_to_aboutMovieFragment)
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)

        val token = PreferenceProvider(requireContext()).getToken()
        vm.movieIdForAbout.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                getSeries(token!!, it.movieId)
            }
        }

    }

    suspend fun getSeries(token: String, movieId:Int){
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.getSeries("Bearer $token", movieId)
        }.onSuccess {

            val adapter =  SeriesMovieAdapter(
                it[0].videos,
                requireContext().applicationContext,
                object: SeriesMovieAdapter.SeriesClickMovie{
                override fun onItemClick(item: SeriesModel.SeriesModelItem.Video) {
                    vm.movieLink.value = LinkVideo( item.link)
                    findNavController().navigate(R.id.videoPlayerFragment)
                }
            } )
              binding?.rcViewSeriesFragment?.adapter = adapter
            binding?.rcViewSeriesFragment?.addItemDecoration(
                CustomDividerItemDecoration(
                    requireContext().getDrawable(R.drawable.divider_1dp_grey)!!,
                ))

        }.onFailure {
            Toast.makeText(requireContext(),"Желіде қателік бар", Toast.LENGTH_SHORT).show()
        }.getOrNull()



    }


}