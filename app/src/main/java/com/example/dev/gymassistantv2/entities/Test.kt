package com.example.dev.gymassistantv2.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Test")
data class Test(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name = "Frytka") var frytka: String
){
    constructor():this(null, "")
}