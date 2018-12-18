package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.Exercise
import com.example.dev.gymassistantv2.Entities.User
import com.example.dev.gymassistantv2.Entities.Workout

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User usr WHERE usr.id == :id")
    fun getById(id: Long): User

    @Query("SELECT * FROM User usr WHERE usr.facebookId == :facebookId")
    fun getByFacebookId(facebookId: Long): User

    @Query("SELECT * FROM User usr WHERE usr.trainerId == :userId")
    fun getChargesForUser(userId: Long): List<User>

    @Query("SELECT * FROM User usr WHERE usr.isTrainer == 1")
    fun getAllTrainers(): List<User>

    @Insert(onConflict = REPLACE)
    fun insert(user: User) : Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}