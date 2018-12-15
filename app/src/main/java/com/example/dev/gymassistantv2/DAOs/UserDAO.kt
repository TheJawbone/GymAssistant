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

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}