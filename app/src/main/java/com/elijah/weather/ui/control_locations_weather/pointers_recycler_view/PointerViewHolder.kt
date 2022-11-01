package com.elijah.weather.ui.control_locations_weather.pointers_recycler_view

import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemPointerBinding
import com.elijah.weather.ui.control_locations_weather.PointItem

class PointerViewHolder(private val binding: ItemPointerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointItem, position: Int) {
        if (position == 0) {
            if (item.select) {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_navigation_d)
            } else {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_navigation_l)
            }
        } else {
            if (item.select) {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_circle_d)
            } else {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_circle_l)
            }
        }
    }
}