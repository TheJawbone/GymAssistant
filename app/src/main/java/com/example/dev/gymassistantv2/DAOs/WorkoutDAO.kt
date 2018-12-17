package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.User
import com.example.dev.gymassistantv2.Entities.Workout

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM Workout wor ORDER BY wor.date DESC")
    fun getAll(): List<Workout>

    @Query("SELECT * FROM Workout wor WHERE wor.id == :id")
    fun getById(id: Long): Workout

    @Query("SELECT * FROM Workout wor WHERE wor.userId == :id ORDER BY wor.date DESC")
    fun getForUser(id: Long): List<Workout>

    @Insert(onConflict = REPLACE)
    fun insert(workout: Workout): Long

    @Update
    fun update(workout: Workout)

    @Delete
    fun delete(workout: Workout)

    @Delete
    fun delete(workouts: List<Workout>)
}