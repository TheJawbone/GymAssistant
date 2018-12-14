package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.User
import com.example.dev.gymassistantv2.Entities.Workout

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM Workout")
    fun getAll(): List<Workout>

    @Insert(onConflict = REPLACE)
    fun insert(workout: Workout): Long

    @Update
    fun update(workout: Workout)

    @Delete
    fun delete(workout: Workout)

    @Delete
    fun delete(workouts: List<Workout>)
}