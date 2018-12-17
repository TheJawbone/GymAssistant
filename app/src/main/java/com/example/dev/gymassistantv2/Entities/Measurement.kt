package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.*

@Entity(tableName = "Measurement", foreignKeys = (arrayOf(
        ForeignKey(
                entity = MuscleGroup::class,
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.NO_ACTION,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("bodyPartId")),
        ForeignKey(
                entity = User::class,
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("userId"))
)))
data class Measurement(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "bodyPartId") var bodyPartId: Long?,
                       @ColumnInfo(name = "userId") var userId: Long?,
                       @ColumnInfo(name = "value") var value: Int?
) {
    constructor() : this(null, null, null, null)
}