package kz.mobydev.draft.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.mobydev.draft.data.network.model.*
import kz.mobydev.draft.domain.Utils.SingleLiveEvent

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