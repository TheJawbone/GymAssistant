package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "User")
data class User(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name="workouts") var workouts: List<Workout>?,
                @ColumnInfo(name="isTrainer") var isTrainer: Boolean,
                @ColumnInfo(name="charges") var charges: List<User>?,
                @ColumnInfo(name="trainer") var trainer: User?
){
    constructor():this(null, null, false, null, null)
}