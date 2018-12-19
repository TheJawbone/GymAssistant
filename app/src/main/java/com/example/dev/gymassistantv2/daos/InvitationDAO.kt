package com.example.dev.gymassistantv2.daos

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.entities.Invitation

@Dao
interface InvitationDAO {
    @Query("SELECT * FROM Invitation")
    fun getAll(): List<Invitation>

    @Query("SELECT * FROM Invitation inv WHERE inv.id == :id")
    fun getById(id: Long): Invitation?

    @Query("SELECT * FROM Invitation inv WHERE inv.senderId == :senderId")
    fun getForSender(senderId: Long): Invitation?

    @Query("SELECT * FROM Invitation inv WHERE inv.recipientId == :recipientId")
    fun getForRecipient(recipientId: Long): List<Invitation>

    @Insert(onConflict = REPLACE)
    fun insert(invitation: Invitation): Long

    @Update
    fun update(invitation: Invitation)

    @Query("DELETE FROM Invitation")
    fun deleteAll()

    @Delete
    fun delete(invitation: Invitation)
}