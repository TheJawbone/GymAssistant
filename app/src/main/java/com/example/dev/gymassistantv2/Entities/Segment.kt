package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.*

@Entity(tableName = "Segment", foreignKeys = (arrayOf(
        ForeignKey(
                entity = Exercise::class,
                onUpdate = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("exerciseId")),
        ForeignKey(
                entity = Workout::class,
                onUpdate = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("workoutId"))
)))
data class Segment(@PrimaryKey(autoGenerate = true) var id: Long?,
                   @ColumnInfo(name = "exerciseId") var exerciseId: Long?,
                   @ColumnInfo(name="workoutId") var workoutId: Long?
) {
    constructor() : this(null, null, null)
}