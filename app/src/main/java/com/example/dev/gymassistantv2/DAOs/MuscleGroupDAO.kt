package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.*

@Dao
interface MuscleGroupDAO {
    @Query("SELECT * FROM MuscleGroup")
    fun getAll(): List<MuscleGroup>

    @Query("SELECT * FROM MuscleGroup WHERE id == :id")
    fun getById(id: Long) : MuscleGroup

    @Query("SELECT name FROM MuscleGroup")
    fun getAllNames(): List<String>

    @Query("SELECT * FROM MuscleGroup musGr WHERE musGr.name LIKE :name")
    fun getByName(name: String): MuscleGroup

    @Insert(onConflict = REPLACE)
    fun insert(muscleGroup: MuscleGroup)

    @Insert(onConflict = REPLACE)
    fun insert(muscleGroups: List<MuscleGroup>)

    @Update
    fun update(muscleGroup: MuscleGroup)

    @Query("DELETE FROM MuscleGroup")
    fun deleteAll()

    @Delete
    fun delete(muscleGroup: MuscleGroup)

    @Delete
    fun delete(muscleGroup: List<MuscleGroup>)
}