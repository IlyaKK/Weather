package com.elijah.weather.data.remote.retrofit

import com.elijah.weather.data.remote.retrofit.response.LocationApiResponse
import com.elijah.weather.data.remote.retrofit.response.WeatherDailyResponse
import com.elijah.weather.data.remote.retrofit.response.WeatherHourlyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://api.openweathermap.org/"
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {
    @GET("data/3.0/onecall")
    suspend fun getHourlyWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): WeatherHourlyResponse

    @GET("/data/3.0/onecall")
    suspend fun getDailyWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): WeatherDailyResponse

    @GET("/geo/1.0/direct")
    suspend fun getLocationCoordinatesByCityName(
        @Query("q") nameCity: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<LocationApiResponse>

    @GET("geo/1.0/reverse")
    suspend fun getNameCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<LocationApiResponse>
}