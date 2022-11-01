package com.elijah.weather

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.elijah.weather.data.LocationRepositoryImpl
import com.elijah.weather.data.WeatherRepositoryImpl
import com.elijah.weather.data.remote.RemoteLocationDataSource
import com.elijah.weather.data.remote.RemoteLocationDataSourceImpl
import com.elijah.weather.data.remote.RemoteWeatherDataSource
import com.elijah.weather.data.remote.retrofit.RetrofitLocationDataSource
import com.elijah.weather.data.remote.retrofit.RetrofitWeatherDataSource
import com.elijah.weather.data.remote.retrofit.WeatherApiService
import com.elijah.weather.data.remote.retrofit.retrofit
import com.elijah.weather.data.remote.room.RoomDatabaseService
import com.elijah.weather.data.remote.room.RoomLocationDataSource
import com.elijah.weather.domain.LocationRepository
import com.elijah.weather.domain.WeatherRepository

class App : Application() {

    private val roomDatabaseService:RoomDatabaseService by lazy {
        Room.databaseBuilder(
            this,
            RoomDatabaseService::class.java, "weather"
        ).build()
    }

    private val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    private val remoteWeatherDataSource: RemoteWeatherDataSource by lazy {
        RetrofitWeatherDataSource(weatherApiService)
    }

    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(remoteWeatherDataSource)
    }

    private val remoteLocationDataSource: RemoteLocationDataSource by lazy {
        RemoteLocationDataSourceImpl(
            RetrofitLocationDataSource(weatherApiService),
            RoomLocationDataSource(roomDatabaseService)
        )
    }

    private val locationRepository: LocationRepository by lazy {
        LocationRepositoryImpl(remoteLocationDataSource)
    }

    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(weatherRepository, locationRepository)
    }
}

val Context.app
    get() = applicationContext as App

val Fragment.app
    get() = requireActivity().app