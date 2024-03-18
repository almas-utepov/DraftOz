package kz.qazaq.qarapkor.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.qazaq.qarapkor.data.network.model.*
import kz.qazaq.qarapkor.domain.Utils.SingleLiveEvent

class VM:ViewModel() {
        val namePageGenre = MutableLiveData<NamePage>()
        val movieIdForAbout = MutableLiveData<MovieIdModel>()
        val movieLink = MutableLiveData<LinkVideo>()
        val userInfo = MutableLiveData<UserInfo>()
        val systemLanguage = MutableLiveData<SelectLanguageModel>()
        val categoryMovie = MutableLiveData<CategoryId>()
        val imageLink = MutableLiveData<ImageLink>()
        internal val selectItemEvent = SingleLiveEvent<MainPageModel>()
}