package com.example.dev.gymassistantv2.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.dev.gymassistantv2.DAOs.TestDao
import com.example.dev.gymassistantv2.Entities.Test

@Database(entities = [Test::class], version = 1)
abstract class TestDatabase : RoomDatabase() {

    abstract fun testDao(): TestDao

    companion object {
        private var INSTANCE: TestDatabase? = null

        fun getInstance(context: Context): TestDatabase? {
            if (INSTANCE == null) {
                synchronized(TestDatabase::class) {
                    var dbBuilder = Room
                            .databaseBuilder(context, TestDatabase::class.java, "testDb")
                            .allowMainThreadQueries()
                    INSTANCE = dbBuilder.build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}