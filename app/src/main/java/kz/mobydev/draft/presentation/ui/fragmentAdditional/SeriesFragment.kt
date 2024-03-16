package kz.qazaq.qarapkor.presentation.ui.fragmentAdditional

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
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.LinkVideo
import kz.qazaq.qarapkor.data.network.model.SeriesModel
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentSeriesBinding
import kz.qazaq.qarapkor.domain.Utils.CustomDividerItemDecoration
import kz.qazaq.qarapkor.domain.adapter.SeriesMovieAdapter
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost

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