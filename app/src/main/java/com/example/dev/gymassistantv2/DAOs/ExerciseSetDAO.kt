package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.*

@Dao
interface ExerciseSetDAO {
    @Query("SELECT * FROM ExerciseSet")
    fun getAll(): List<ExerciseSet>

    @Insert(onConflict = REPLACE)
    fun insert(exerciseSet: ExerciseSet)

    @Update
    fun update(exerciseSet: ExerciseSet)

    @Delete
    fun delete(exerciseSet: ExerciseSet)

    @Delete
    fun delete(exerciseSet: List<ExerciseSet>)
}