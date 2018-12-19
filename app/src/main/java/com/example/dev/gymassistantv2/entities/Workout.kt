package com.example.dev.gymassistantv2.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Workout")
data class Workout(@PrimaryKey(autoGenerate = true) var id: Long?,
                   @ColumnInfo(name="date") var date: Long?,
                   @ColumnInfo(name="userId") var userId: Long?
){
    constructor():this(null, null, null)
}