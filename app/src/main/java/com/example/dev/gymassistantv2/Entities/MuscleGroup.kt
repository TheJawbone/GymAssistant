package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "MuscleGroup")
data class MuscleGroup(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name="name") var name: String
){
    constructor():this(null, "")
}