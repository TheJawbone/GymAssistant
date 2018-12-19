package com.example.dev.gymassistantv2.daos

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.entities.Exercise

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM Exercise")
    fun getAll(): List<Exercise>

    @Query("SELECT name FROM Exercise")
    fun getAllNames(): List<String>

    @Query("SELECT * FROM Exercise WHERE muscleGroupId == :id")
    fun getForMuscleGroup(id: Long): List<Exercise>

    @Query("SELECT name FROM Exercise ex WHERE ex.muscleGroupId == :muscleGroupId AND ex.visible")
    fun getVisibleNamesForMuscleGroup(muscleGroupId: Long): List<String>

    @Query("SELECT * FROM Exercise ex WHERE ex.defaultExercise == 1")
    fun getDefault(): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.ownerId == :ownerId")
    fun getCustomForUser(ownerId: Long): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.ownerId == :ownerId OR ex.defaultExercise == 1")
    fun getForUser(ownerId: Long): List<Exercise>

    @Query("SELECT * FROM Exercise ex WHERE ex.name LIKE :name")
    fun getByName(name: String): Exercise

    @Query("SELECT * FROM Exercise ex WHERE ex.id == :id")
    fun getById(id: Long): Exercise

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

    @Query("DELETE FROM Exercise WHERE ownerId == :ownerId AND defaultExercise == 0")
    fun delete(ownerId: Long)
}