package com.elijah.weather.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    "weather_days_city",
    foreignKeys = [ForeignKey(LocationDbModel::class, ["id"], ["city_id"], onDelete = CASCADE)]
)
data class WeatherOfDaysCityDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "day_date")
    val dayDate: Long,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: String,
    @ColumnInfo(name = "temperature_min")
    val temperatureMin: String,
    @ColumnInfo(name = "temperature_max")
    val temperatureMax: String
)
