package com.example.dev.gymassistantv2.daos

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.entities.*

@Dao
interface ExerciseSetDAO {
    @Query("SELECT * FROM ExerciseSet")
    fun getAll(): List<ExerciseSet>

    @Query("SELECT * FROM ExerciseSet exSet WHERE exSet.id == :id")
    fun getById(id: Long): ExerciseSet

    @Query("SELECT * FROM ExerciseSet exSet WHERE exSet.segmentId == :id")
    fun getForSegment(id: Long): List<ExerciseSet>

    @Insert(onConflict = REPLACE)
    fun insert(exerciseSet: ExerciseSet)

    @Update
    fun update(exerciseSet: ExerciseSet)

    @Delete
    fun delete(exerciseSet: ExerciseSet)

    @Delete
    fun delete(exerciseSet: List<ExerciseSet>)
}