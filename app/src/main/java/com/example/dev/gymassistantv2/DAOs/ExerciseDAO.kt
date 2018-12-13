package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.Exercise
import com.example.dev.gymassistantv2.Entities.User
import com.example.dev.gymassistantv2.Entities.Workout

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM Exercise")
    fun getAll(): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.defaultExercise == 1")
    fun getDefault(): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.ownerId == :ownerId")
    fun getCustomForUser(ownerId: Long): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.ownerId == :ownerId OR ex.defaultExercise == 1")
    fun getForUser(ownerId: Long): List<Exercise>

    @Insert(onConflict = REPLACE)
    fun insert(exercise: Exercise)

    @Insert(onConflict = REPLACE)
    fun insert(exercises: List<Exercise>)

    @Update
    fun update(exercise: Exercise)

    @Query("DELETE FROM Exercise")
    fun deleteAll()

    @Delete
    fun delete(exercise: Exercise)

    @Query("DELETE FROM Exercise WHERE ownerId == :ownerId")
    fun delete(ownerId: Long)
}