package com.example.dev.gymassistantv2.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ExerciseSet")
@ForeignKey(entity = Segment::class, parentColumns = ["segmentId"], childColumns = ["id"],
        onUpdate = CASCADE, onDelete = CASCADE)
data class ExerciseSet(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name="repCount") var repCount: Int?,
                       @ColumnInfo(name="weight") var weight: Int?,
                       @ColumnInfo(name="segmentId") var segmentId: Long?
){
    constructor():this(null, null, null, null)
}