package kz.mobydev.draft.domain.usecase

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import kz.mobydev.draft.data.network.model.FavoriteModel
import kz.mobydev.draft.data.preferences.PreferenceProvider
import kz.mobydev.draft.domain.Utils.Constants

class BundleJsonUseCase() {
    private val gson = Gson()
    private var bundle = Bundle()
    fun convertJsonASBundle(item: FavoriteModel.FavoriteModelItem):Bundle{
        val json = gson.toJson(item)
        bundle.putString(Constants.KEY_BUNDLE, json)
        return bundle
    }
    fun getJson(context: Context):String{
        return PreferenceProvider(context.applicationContext).getToken().toString()
    }

}