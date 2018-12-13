package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ExerciseSet")
data class ExerciseSet(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name="repCount") var repCount: Int,
                       @ColumnInfo(name="weight") var weight: Int
){
    constructor():this(null, 0, 0)
}