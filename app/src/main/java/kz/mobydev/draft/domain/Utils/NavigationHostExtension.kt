package kz.mobydev.draft.presentation.ui

import androidx.fragment.app.Fragment
import kz.mobydev.draft.domain.Utils.NavigationHostProvider

fun Fragment.provideNavigationHost(): NavigationHostProvider? {
    return try {
        activity as NavigationHostProvider
    } catch (e: Exception) {
        null
    }
}