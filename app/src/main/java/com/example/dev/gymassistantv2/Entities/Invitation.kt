package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.*
import java.sql.Date

@Entity(tableName = "Invitation", foreignKeys = (arrayOf(
        ForeignKey(
                entity = User::class,
                onUpdate = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("senderId")),
        ForeignKey(
                entity = User::class,
                onUpdate = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("recipientId"))
)))
data class Invitation(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name="senderId") var senderId: Long?,
                      @ColumnInfo(name="recipientId") var recipientId: Long?,
                      @ColumnInfo(name="date") var date: Long?
){
    constructor():this(null, null, null, null)
}