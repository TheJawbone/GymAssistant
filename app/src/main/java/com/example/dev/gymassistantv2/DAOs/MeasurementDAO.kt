package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.Invitation
import com.example.dev.gymassistantv2.Entities.Measurement

@Dao
interface MeasurementDAO {
    @Query("SELECT * FROM Measurement")
    fun getAll(): List<Measurement>

    @Query("SELECT * FROM Measurement mes WHERE mes.id == :id")
    fun getById(id: Long): Measurement

    @Query("SELECT * FROM Measurement mes WHERE mes.bodyPartId == :id")
    fun getForBodyPart(id: Long): List<Measurement>

    @Insert(onConflict = REPLACE)
    fun insert(measurement: Measurement)

    @Update
    fun update(measurement: Measurement)

    @Query("DELETE FROM Measurement")
    fun deleteAll()

    @Delete
    fun delete(measurement: Measurement)
}