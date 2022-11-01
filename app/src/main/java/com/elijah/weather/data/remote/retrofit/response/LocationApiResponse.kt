package com.elijah.weather.data.remote.retrofit.response

data class LocationApiResponse(
    val name: String?,
    val local_names: LocalNames,
    val lat: Double?,
    val lon: Double?,
    val country: String?,
    val state: String?
)

data class LocalNames(
    val ascii: String?,
    val feature_name: String?,
    val ru: String?
)