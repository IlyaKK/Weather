package com.elijah.weather.di

import android.app.Application
import com.elijah.weather.data.database.AppDatabase
import com.elijah.weather.data.database.LocationDao
import com.elijah.weather.data.database.WeatherDao
import com.elijah.weather.data.network.ApiFactory
import com.elijah.weather.data.network.ApiService
import com.elijah.weather.data.repository.WeatherOfCityRepositoryImpl
import com.elijah.weather.domain.WeatherOfCityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindWeatherOfCityRepository(impl: WeatherOfCityRepositoryImpl): WeatherOfCityRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideWeatherDao(
            application: Application
        ): WeatherDao {
            return AppDatabase.getInstance(application).weatherDao()
        }

        @ApplicationScope
        @Provides
        fun provideLocationDao(
            application: Application
        ): LocationDao {
            return AppDatabase.getInstance(application).locationDao()
        }

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}