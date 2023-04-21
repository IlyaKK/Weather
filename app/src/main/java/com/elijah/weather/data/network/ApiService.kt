package com.elijah.weather.data.network

import com.elijah.weather.BuildConfig
import com.elijah.weather.data.network.model.LocationDto
import com.elijah.weather.data.network.model.WeatherContainerDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "current,minutely,alerts",
        @Query("appid") apiKey: String = BuildConfig.WEATHER_ONE_CALL_API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru"
    ): WeatherContainerDto

    @GET("geo/1.0/direct")
    suspend fun getCitiesByName(
        @Query("q") nameCity: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_ONE_CALL_API_KEY
    ): List<LocationDto>

    @GET("/geo/1.0/reverse")
    suspend fun getCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_ONE_CALL_API_KEY
    ): List<LocationDto>
}