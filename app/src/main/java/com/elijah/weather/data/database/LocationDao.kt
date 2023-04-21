package com.elijah.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(entity = LocationDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationDbModel: LocationDbModel): Long

    @Query(
        "DELETE FROM locations " +
                "WHERE id = :idCity "
    )
    suspend fun deleteLocation(idCity: Int)

    @Query(
        "SELECT * FROM locations " +
                "ORDER BY current DESC, last_added"
    )
    fun getAllLocations(): Flow<List<LocationDbModel>>

    @Query(
        "SELECT * FROM locations " +
                "WHERE current = 1 " +
                "LIMIT 1"
    )
    suspend fun getCurrentLocation(): LocationDbModel?
}