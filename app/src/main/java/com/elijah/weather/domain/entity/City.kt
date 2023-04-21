package com.elijah.weather.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int = UNDEFINED_ID,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val country: String,
    val state: String,
    val currentCity: Boolean
) : Parcelable {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}
