package kz.mobydev.draft.presentation.ui.fragmentAdditional

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import kz.mobydev.draft.R
import kz.mobydev.draft.databinding.FragmentVideoPlayerBinding
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost

class VideoPlayerFragment : Fragment() {

    private var binding:FragmentVideoPlayerBinding? = null
    private val vm: VM by activityViewModels()


    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlayerBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        vm.movieLink.observe(viewLifecycleOwner){
           playerYouTube(it.link)
        }

    }

    fun playerYouTube(movieLink:String){
       val youTubePlayerView = binding?.youtubePlayerView

        lifecycle.addObserver(youTubePlayerView!!)

        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {


                // using pre-made custom ui
                val defaultPlayerUiController = DefaultPlayerUiController(
                    youTubePlayerView!!, youTubePlayer
                )
                defaultPlayerUiController.rootView.findViewById<View>(com.pierfrancescosoffritti.androidyoutubeplayer.R.id.drop_shadow_top).apply {
                    setBackgroundResource(R.drawable.button_exit_player)
                    setPadding(24,24,24,24)
                    updateLayoutParams {
                        width = 170
                        height = 170
                    }
                    setOnClickListener {
                        findNavController().navigate(R.id.action_videoPlayerFragment_to_seriesFragment)
                    }
                }

                youTubePlayerView!!.setCustomPlayerUi(defaultPlayerUiController.rootView)
                defaultPlayerUiController.showYouTubeButton(false)
                defaultPlayerUiController.showFullscreenButton(false)
                defaultPlayerUiController.rootView.findViewById<com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar>(com.pierfrancescosoffritti.androidyoutubeplayer.R.id.youtube_player_seekbar).setColor(
                    resources.getColor(R.color.primary_500,null))

                youTubePlayer.loadOrCueVideo(lifecycle, movieLink, 0f)
            }
        }

        // disable web ui
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()

        youTubePlayerView!!.initialize(listener, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

    }

}