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
import com.bumptech.glide.Glide

import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.ImageLink
import kz.qazaq.qarapkor.data.network.model.LinkVideo
import kz.qazaq.qarapkor.data.network.model.MovieIdModel
import kz.qazaq.qarapkor.data.network.model.MovieInfoResponse
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentAboutMovieBinding
import kz.qazaq.qarapkor.domain.adapter.ImageAdapter
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost

class AboutMovieFragment : Fragment() {
    private var binding: FragmentAboutMovieBinding? = null
    private val vm: VM by activityViewModels()
    private val response = ServiceBuilder.buildService(ApiInterface::class.java)


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutMovieBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.shimmerLayout?.apply {
            visibility = View.VISIBLE
            binding?.fullInfoLayout?.visibility = View.GONE
            startShimmer()
        }
        binding?.btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }


        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_aboutMovieFragment_to_mainFragment)
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)


        val token = PreferenceProvider(requireContext()).getToken()!!
        vm.movieIdForAbout.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                retrofitMovieId(token, it.movieId)
            }
        }
    }

    suspend fun favoriteSelect(token: String, id: Int, state:Boolean) {
        val buttonFavorite = binding?.btnFavoriteMovie
        val focusBackground = resources.getDrawable(R.drawable.icon_button_favorite_focused)
        val unFocusBackground = resources.getDrawable(R.drawable.icon_button_favorite)

        if (!state) {
            runCatching {
                response.addToFavorite("Bearer $token", MovieIdModel(id))
            }.onSuccess {
                buttonFavorite?.background = focusBackground
                lifecycleScope.launch {
                    retrofitMovieId(token, it.movieId)
                }
            }.onFailure { t ->
                Toast.makeText(requireContext(), "Таңдамалыға қосылмады", Toast.LENGTH_SHORT).show()
            }.getOrNull()

        } else {


            runCatching {
                response.deleteAtFavorite("Bearer $token", MovieIdModel(id))
            }.onSuccess {
                buttonFavorite?.background = unFocusBackground
                lifecycleScope.launch {
                    retrofitMovieId(token, id)
                }
            }.onFailure { t ->
                Toast.makeText(requireContext(), "Таңдамалыдан алынбады", Toast.LENGTH_SHORT).show()
            }


        }


    }

    suspend fun retrofitMovieId(token: String, id: Int) {

        val selectMovie =
            runCatching { response.movieWithId("Bearer $token", id) }
                .onFailure { t ->
                    Toast.makeText(requireContext(), "Желіде ақау кетті", Toast.LENGTH_SHORT).show()
                }.getOrNull()

        if (selectMovie != null) {
            if (selectMovie.video == null) {
                binding?.run {
                    textTvBolimder.text = "${selectMovie.seasonCount} сезон, ${selectMovie.seriesCount} серия "
                    textTvBolimder.setOnClickListener{
                        findNavController().navigate(R.id.seriesFragment)
                    }
                    btnNextAllMovie.setOnClickListener {
                        findNavController().navigate(R.id.seriesFragment)
                    }
                }

            }else{
                binding?.run {
                    textTvBolimder.visibility = View.GONE
                    textBolimder.visibility = View.GONE
                    btnNextAllMovie.visibility = View.GONE
                }
            }


            binding?.btnPlayMovie?.setOnClickListener {
                if (selectMovie.video == null) {

                    vm.movieIdForAbout.value = MovieIdModel(selectMovie.id)
                    findNavController().navigate(R.id.seriesFragment)

                } else {
                    binding?.btnPlayMovie?.setOnClickListener {
                        vm.movieLink.value = LinkVideo(selectMovie.video.link)
                        findNavController().navigate(R.id.videoPlayerFragment)
                    }
                }
            }





            binding?.shimmerLayout?.apply {
                binding?.fullInfoLayout?.visibility = View.VISIBLE
                visibility = View.GONE
                stopShimmer()
            }
            val item = selectMovie


            item.apply {
                binding?.apply {
                    textTvTittleMovie.text = item.name
                    Glide.with(requireContext())
                        .load(item.poster.link)
                        .into(imageView3);
                    if (btnMoreDescription.lineCount == 1){
                        btnMoreDescription.visibility = View.GONE
                    }else {
                        btnMoreDescription.setOnClickListener {
                            if (textTvDescription.maxLines == 100) {
                                btnMoreDescription.text = "Толығырақ"
                                textTvDescription.maxLines = 7
                                fadingEdgeLayoutDescription.setFadeSizes(0, 0, 120, 0)
                            } else {
                                btnMoreDescription.text = "Жасыру"
                                textTvDescription.maxLines = 100
                                fadingEdgeLayoutDescription.setFadeSizes(0, 0, 0, 0)
                            }
                        }
                    }
                    textTvDescription.text = item.description
                    item.apply {
                        var additionalInfo = " "
                        for (i in genres) {
                            additionalInfo += "• ${i.name}"
                        }
                        textTvAdditionalInfoYear.text = "$year"
                        textTvGenres.text = additionalInfo
                        if (item.favorite) {
                            btnFavoriteMovie?.background =
                                resources.getDrawable(R.drawable.icon_button_favorite_focused, null)
                        }else{
                            btnFavoriteMovie.background =
                                resources.getDrawable(R.drawable.icon_button_favorite, null)
                        }


                        binding?.btnFavoriteMovie?.setOnClickListener {
                            lifecycleScope.launch {
                                favoriteSelect(token, item.id, item.favorite)

                            }

                        }
                        binding?.run {
                            textTvDirector.text = item.director
                            textTvProducer.text = item.producer


                        }
                    }

                }
                binding?.shimmerLayout?.apply {
                    visibility = View.GONE
                    stopShimmer()
                }


                val imageAdapter = ImageAdapter(item.screenshots,requireContext(),object : ImageAdapter.ImageClick{
                    override fun onImgClick(item: MovieInfoResponse.Screenshot) {
                        vm.imageLink.value = ImageLink( item.link)
                        findNavController().navigate(R.id.action_aboutMovieFragment_to_imageFragment)
                    }

                })
                binding?.rcViewScreenShots?.adapter = imageAdapter
                binding?.btnSimilarMovieMore?.setOnClickListener {
                    findNavController().navigate(R.id.action_aboutMovieFragment_to_similarFragment)
                }
            }
        }


    }


}


