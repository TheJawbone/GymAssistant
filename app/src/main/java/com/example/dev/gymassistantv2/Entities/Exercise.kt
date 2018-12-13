package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Exercise")
data class Exercise(@PrimaryKey(autoGenerate = true) var id: Long?,
                    @ColumnInfo(name="sets") var sets: List<ExerciseSet>?,
                    @ColumnInfo(name="muscleGroup") var muscleGroup: MuscleGroup?,
                    @ColumnInfo(name="exerciseName") var exerciseName: String
){
    constructor():this(null, null, null, "")
}