package kz.qazaq.qarapkor.presentation.ui.fragmentMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.CategoryId
import kz.qazaq.qarapkor.data.network.model.CategoryMovieResponse
import kz.qazaq.qarapkor.data.network.model.GenreResponse
import kz.qazaq.qarapkor.data.network.model.MainPageModel
import kz.qazaq.qarapkor.data.network.model.MovieIdModel
import kz.qazaq.qarapkor.data.network.model.MoviesMain
import kz.qazaq.qarapkor.data.network.model.NamePage
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.domain.Utils.SingleLiveEvent
import kz.qazaq.qarapkor.domain.adapter.GenreMainAdapter
import kz.qazaq.qarapkor.domain.adapter.MainMovieAdapter
import kz.qazaq.qarapkor.domain.adapter.MainPageCategoryAdapter
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost
import kz.qazaq.qarapkor.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private lateinit var navController: NavController
    private val vm: VM by activityViewModels()
    private val toastEvent = SingleLiveEvent<MainPageModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(true)
            setNavigationToolBar(true, true)
            additionalToolBarConfig(false, btnExitVisible = false, titleVisible = false, title = "")
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {

            setNavigationVisibility(true)
            setNavigationToolBar(true, true)
            additionalToolBarConfig(false, btnExitVisible = false, titleVisible = false, title = "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.textTVERROR?.visibility = View.GONE
        binding?.shimmerInMainFragment?.startShimmer()
        binding?.shimmerInMainFragmentAdd?.startShimmer()
        binding?.imgERRORView?.visibility = View.GONE
        binding?.constrainMainFragment?.visibility = View.GONE




        val token = PreferenceProvider(requireContext()).getToken()!!
        mainMoviePoster(token)

    }


    private fun mainMoviePoster(token: String) {
        lifecycleScope.launch {
            mainMovie(token)
            getMovieOzinshe(token)
            getMovieTelihikaialar(token)
            getGenreRC(token)
        }

    }

    private suspend fun mainMovie(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        runCatching {
            val direction = "DESC"
            val page = 0
            val size = 20
            val sortField = "createdDate"
            response.getCategoryMovieType("Bearer $token",id, direction, page, size, sortField, "MOVIE")
        }.onSuccess {
            binding?.shimmerInMainFragment?.stopShimmer()
            binding?.shimmerInMainFragment?.visibility = View.GONE
            binding?.constrainMainFragment?.visibility = View.VISIBLE
            val adapter =
                MainMovieAdapter(it.content, requireContext(), object : MainMovieAdapter.ItemClick {
                    override fun onItemClickMainMovie(item: CategoryMovieResponse.Content) {
                        vm.movieIdForAbout.value = MovieIdModel(item.id)
                        findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                    }
                })

            binding?.recyclerViewPlaceForMainMovies?.adapter = adapter

        }.onFailure {
            binding?.textTVERROR?.visibility = View.VISIBLE
            binding?.imgERRORView?.visibility = View.VISIBLE
            binding?.nestedScroll?.visibility = View.GONE

        }.getOrNull()
    }

    private suspend fun getMovieOzinshe(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        val item = runCatching {
            response.moviesMainPage("Bearer $token")
        }.getOrNull()
        if (item!=null && item.isNotEmpty() ){
            binding?.shimmerInMainFragmentAdd?.stopShimmer()
            binding?.shimmerInMainFragmentAdd?.visibility = View.GONE

            val adapter = MainPageCategoryAdapter(item[0].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {

                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.rcViewCategoryOzinshe?.adapter = adapter

        }

    }

    private suspend fun getMovieTelihikaialar(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.moviesMainPage("Bearer $token")
        }.onSuccess {item->

            val adapter =MainPageCategoryAdapter(item[1].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {

                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.textTvCategoryTitle2?.text = item[1].categoryName
            binding?.rcViewCategoryMovie2?.adapter = adapter

        }
    }
    private suspend fun getGenreRC(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.getGenre("Bearer $token")
        }.onSuccess {

            binding?.textTvCategoryTitle3?.text = getString(R.string.ChooceGenre)
            val adapter = GenreMainAdapter(it,requireContext(),object:GenreMainAdapter.ItemOnClickChooseGenre{
                override fun onClickToGenre(item: GenreResponse.GenreResponseItem) {
                    vm.categoryMovie.value = CategoryId(item.id)
                    findNavController().navigate(R.id.categoriesFragment)
                }

            })
            binding?.rcViewCategoryCategories?.adapter = adapter

        }.onFailure {

        }.getOrNull()
    }





}


