package kz.mobydev.draft.presentation.ui.fragmentMain

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
import kz.mobydev.draft.R
import kz.mobydev.draft.data.network.api.ApiInterface
import kz.mobydev.draft.data.network.api.ServiceBuilder
import kz.mobydev.draft.data.network.model.CategoryId
import kz.mobydev.draft.data.network.model.GenreResponse
import kz.mobydev.draft.data.network.model.MainPageModel
import kz.mobydev.draft.data.network.model.MovieIdModel
import kz.mobydev.draft.data.network.model.MoviesMain
import kz.mobydev.draft.data.network.model.NamePage
import kz.mobydev.draft.data.preferences.PreferenceProvider
import kz.mobydev.draft.databinding.FragmentMainBinding
import kz.mobydev.draft.domain.Utils.Constants
import kz.mobydev.draft.domain.Utils.SingleLiveEvent
import kz.mobydev.draft.domain.adapter.GenreMainAdapter
import kz.mobydev.draft.domain.adapter.MainMovieAdapter
import kz.mobydev.draft.domain.adapter.MainPageCategoryAdapter
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost
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

        binding?.run {
            btnCategoryAllMovie1.visibility = View.INVISIBLE
            btnCategoryAllMovie2.visibility = View.INVISIBLE
            btnCategoryAllMovie3.visibility = View.INVISIBLE
            btnCategoryAllMovie4.visibility = View.INVISIBLE
            btnCategoryAllMovie5.visibility = View.INVISIBLE
            btnCategoryAllMovie6.visibility = View.INVISIBLE



        }


        val token = PreferenceProvider(requireContext()).getToken()!!
        mainMoviePoster(token)

    }


    private fun mainMoviePoster(token: String) {
        lifecycleScope.launch {
            mainMovie(token)
            getMovieOzinshe(token)
            getMovieTelihikaialar(token)
            getGenreRC(token)
            getMovieTolyqMult(token)
            getMovieMultSerial(token)
            getMovieSitcom(token)
        }

    }

    private suspend fun mainMovie(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        runCatching {
            response.moviesMain("Bearer $token")
        }.onSuccess {
            binding?.shimmerInMainFragment?.stopShimmer()
            binding?.shimmerInMainFragment?.visibility = View.GONE
            binding?.constrainMainFragment?.visibility = View.VISIBLE
            val adapter =
                MainMovieAdapter(it, requireContext(), object : MainMovieAdapter.ItemClick {
                    override fun onItemClickMainMovie(item: MoviesMain.MoviesMainItem) {
                        vm.movieIdForAbout.value = MovieIdModel(item.movie.id)
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

            binding?.btnCategoryAllMovie1?.visibility = View.VISIBLE
            binding?.btnCategoryAllMovie1?.setOnClickListener {
                vm.namePageGenre.value = NamePage(item[0].categoryName)
                vm.categoryMovie.value = CategoryId(1)
                findNavController().navigate(R.id.categoriesFragment)
            }
            val adapter = MainPageCategoryAdapter(item[0].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {

                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.textTvCategoryTitle1?.text = item[0].categoryName
            binding?.rcViewCategoryOzinshe?.adapter = adapter

        }

    }

    private suspend fun getMovieTelihikaialar(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.moviesMainPage("Bearer $token")
        }.onSuccess {item->
            binding?.btnCategoryAllMovie2?.visibility = View.VISIBLE
            binding?.btnCategoryAllMovie2?.setOnClickListener {
                vm.namePageGenre.value = NamePage(item[1].categoryName)
                vm.categoryMovie.value = CategoryId(5)
                findNavController().navigate(R.id.categoriesFragment)
            }
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

            binding?.btnCategoryAllMovie3?.visibility = View.VISIBLE
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
    private suspend fun getMovieTolyqMult(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.moviesMainPage("Bearer $token")
        }.onSuccess {item->
            binding?.btnCategoryAllMovie4?.visibility = View.VISIBLE
            binding?.btnCategoryAllMovie4?.setOnClickListener {
                vm.namePageGenre.value = NamePage(item[2].categoryName)
                vm.categoryMovie.value = CategoryId(8)
                findNavController().navigate(R.id.categoriesFragment)
            }
            val adapter =MainPageCategoryAdapter(item[2].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {

                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.textTvCategoryTitle4?.text = item[2].categoryName
            binding?.rcViewCategoryTolyqMult?.adapter = adapter

        }
    }

    private suspend fun getMovieMultSerial(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.moviesMainPage("Bearer $token")
        }.onSuccess {item->
            binding?.btnCategoryAllMovie5?.visibility = View.VISIBLE
            binding?.btnCategoryAllMovie5?.setOnClickListener {
                vm.namePageGenre.value = NamePage(item[3].categoryName)
                vm.categoryMovie.value = CategoryId(9)
                findNavController().navigate(R.id.categoriesFragment)
            }
            val adapter =MainPageCategoryAdapter(item[3].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {
                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.textTvCategoryTitle5?.text = item[3].categoryName
            binding?.rcViewCategoryMultSerial?.adapter = adapter

        }
    }

    private suspend fun getMovieSitcom(token: String) {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        runCatching {
            response.moviesMainPage("Bearer $token")
        }.onSuccess{item->
            binding?.btnCategoryAllMovie6?.visibility = View.VISIBLE

            binding?.btnCategoryAllMovie6?.setOnClickListener {
                vm.namePageGenre.value = NamePage(item[4].categoryName)
                vm.categoryMovie.value = CategoryId(31)
                findNavController().navigate(R.id.categoriesFragment)
            }
            val adapter =MainPageCategoryAdapter(item[4].movies, requireContext(), object :MainPageCategoryAdapter.ClickInterface{
                override fun onItemClick(item: MainPageModel.MainPageModelItem.Movy) {
                    vm.movieIdForAbout.value = MovieIdModel(item.id)
                    findNavController().navigate(R.id.action_mainFragment_to_aboutMovieFragment)
                }

            })
            binding?.textTvCategoryTitle6?.text = item[4].categoryName
            binding?.rcViewCategorySitcom?.adapter = adapter

        }
    }

}


