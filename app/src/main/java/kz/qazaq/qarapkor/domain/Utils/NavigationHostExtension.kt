package kz.qazaq.qarapkor.presentation.ui

import androidx.fragment.app.Fragment
import kz.qazaq.qarapkor.domain.Utils.NavigationHostProvider

fun Fragment.provideNavigationHost(): NavigationHostProvider? {
    return try {
        activity as NavigationHostProvider
    } catch (e: Exception) {
        null
    }
}