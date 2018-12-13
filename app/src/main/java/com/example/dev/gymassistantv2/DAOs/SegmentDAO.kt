package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.Exercise
import com.example.dev.gymassistantv2.Entities.Segment
import com.example.dev.gymassistantv2.Entities.User
import com.example.dev.gymassistantv2.Entities.Workout

@Dao
interface SegmentDAO {
    @Query("SELECT * FROM Segment")
    fun getAll(): List<Segment>

    @Insert(onConflict = REPLACE)
    fun insert(segment: Segment)

    @Update
    fun update(segment: Segment)

    @Delete
    fun delete(segment: Segment)

    @Delete
    fun delete(segments: List<Segment>)
}