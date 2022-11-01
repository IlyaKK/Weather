package com.elijah.weather.ui.control_locations_weather.pointers_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemPointerBinding
import com.elijah.weather.ui.control_locations_weather.PointItem

class PointersAdapter : ListAdapter<PointItem, PointerViewHolder>(PointersDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pointer, parent, false)
        val binding = ItemPointerBinding.bind(view)
        return PointerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointerViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

object PointersDiffCallback : DiffUtil.ItemCallback<PointItem>() {
    override fun areItemsTheSame(
        oldItem: PointItem,
        newItem: PointItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PointItem,
        newItem: PointItem
    ): Boolean {
        return oldItem.id == newItem.id
    }
}