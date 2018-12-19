package com.example.dev.gymassistantv2.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.example.dev.gymassistantv2.entities.Test

@Dao
interface TestDao {
    @Query("SELECT * from Test")
    fun getAll(): List<Test>

    @Insert(onConflict = REPLACE)
    fun insert(weatherData: Test)

    @Query("DELETE from Test")
    fun deleteAll()
}