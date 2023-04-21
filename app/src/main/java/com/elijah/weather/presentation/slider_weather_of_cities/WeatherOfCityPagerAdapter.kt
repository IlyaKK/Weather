package com.elijah.weather.presentation.slider_weather_of_cities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elijah.weather.domain.entity.City
import com.elijah.weather.presentation.weather_info.WeatherInfoFragment

class WeatherOfCityPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var cityList = listOf<City>()
        set(value) {
            val callback = CityListDiffCallback(cityList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherInfoFragment.getInstance(cityList[position])
    }
}