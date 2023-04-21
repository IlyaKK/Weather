package com.elijah.weather.di

import androidx.lifecycle.ViewModel
import com.elijah.weather.presentation.weather_info.WeatherInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModuleWeatherInfo {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherInfoViewModel::class)
    fun bindWeatherInfoViewModel(viewModel: WeatherInfoViewModel): ViewModel
}