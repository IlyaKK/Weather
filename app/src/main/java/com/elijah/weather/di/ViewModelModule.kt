package com.elijah.weather.di

import androidx.lifecycle.ViewModel
import com.elijah.weather.presentation.edit_location.EditLocationViewModel
import com.elijah.weather.presentation.slider_weather_of_cities.WeatherCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherCityViewModel::class)
    fun bindCityInfoViewModel(viewModel: WeatherCityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditLocationViewModel::class)
    fun bindEditLocationViewModel(viewModel: EditLocationViewModel): ViewModel
}