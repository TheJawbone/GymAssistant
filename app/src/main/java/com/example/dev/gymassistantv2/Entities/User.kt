package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "User")
data class User(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name="facebookId") var facebookId: Long?,
                @ColumnInfo(name="isTrainer") var isTrainer: Boolean?,
                @ColumnInfo(name="trainerId") var trainerId: Long?
){
    constructor():this(null, null, false, null)
}