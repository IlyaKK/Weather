package com.elijah.weather.ui.control_locations_weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elijah.weather.domain.entity.Location
import com.elijah.weather.ui.weather_info.WeatherInfoFragment

class ScreenSlidePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var currentLoc = mutableListOf<Location>()

    fun setLocations(locations: List<Location>) {
        if (locations.size > currentLoc.size) {
            for (index in currentLoc.size until locations.size) {
                currentLoc.add(locations[index])
                notifyItemChanged(index)
            }
        } else if (locations.size == currentLoc.size) {
            for (index in locations.indices) {
                if (locations[index].cityName != currentLoc[index].cityName) {
                    notifyItemRemoved(index)
                    currentLoc.removeAt(index)
                    currentLoc.add(index, locations[index])
                    notifyItemChanged(index)
                }
            }
        } else if (locations.size < currentLoc.size) {
            for (index in currentLoc.indices) {
                if (index < locations.size && (locations[index].cityName != currentLoc[index].cityName)) {
                    notifyItemRemoved(index)
                    currentLoc.removeAt(index)
                    currentLoc.add(index, locations[index])
                    notifyItemChanged(index)
                } else if (index >= locations.size) {
                    currentLoc.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currentLoc.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherInfoFragment.getInstance(
            currentLoc[position].latitude,
            currentLoc[position].longitude,
            currentLoc[position].cityName
        )
    }
}