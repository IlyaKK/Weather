package com.elijah.weather.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity("locations")
data class LocationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "state")
    val state: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "current")
    val currentCity: Boolean,
    @ColumnInfo(name = "last_added")
    val lastAdded: Long = createTime()
) {
    companion object {
        fun createTime(): Long {
            return Calendar.getInstance().timeInMillis
        }
    }

}
