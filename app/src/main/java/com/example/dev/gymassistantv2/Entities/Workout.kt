package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "Workout")
data class Workout(@PrimaryKey(autoGenerate = true) var id: Long?,
                   @ColumnInfo(name="exercises") var exercises: List<Exercise>?,
                   @ColumnInfo(name="date") var date: Date?
){
    constructor():this(null, null, null)
}