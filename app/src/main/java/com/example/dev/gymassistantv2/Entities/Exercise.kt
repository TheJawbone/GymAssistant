package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.*

@Entity(tableName = "Exercise", foreignKeys = (arrayOf(
        ForeignKey(
                entity = User::class,
                onUpdate = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("ownerId")),
        ForeignKey(
                entity = MuscleGroup::class,
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.NO_ACTION,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("muscleGroupId"))
)))
data class Exercise(@PrimaryKey(autoGenerate = true) var id: Long?,
                    @ColumnInfo(name = "ownerId") var ownerId: Long?,
                    @ColumnInfo(name = "muscleGroupId") var muscleGroupId: Long?,
                    @ColumnInfo(name = "name") var name: String?,
                    @ColumnInfo(name = "defaultExercise") var defaultExercise: Int?
) {
    constructor() : this(null, null, null, null, 0)
}