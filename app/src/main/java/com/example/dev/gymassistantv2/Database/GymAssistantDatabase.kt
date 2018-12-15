package com.example.dev.gymassistantv2.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.dev.gymassistantv2.DAOs.*
import com.example.dev.gymassistantv2.Entities.*

@Database(entities = [Exercise::class, ExerciseSet::class, Invitation::class, Measurement::class,
    MuscleGroup::class, Segment::class, User::class, Workout::class], version = 1)
abstract class GymAssistantDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDAO
    abstract fun exerciseSetDao(): ExerciseSetDAO
    abstract fun invitationDao(): InvitationDAO
    abstract fun measurementDao(): MeasurementDAO
    abstract fun muscleGroupDao(): MuscleGroupDAO
    abstract fun segmentDao(): SegmentDAO
    abstract fun userDao(): UserDAO
    abstract fun workoutDao(): WorkoutDAO

    companion object {
        private var INSTANCE: GymAssistantDatabase? = null

        fun getInstance(context: Context): GymAssistantDatabase? {
            if (INSTANCE == null) {
                synchronized(GymAssistantDatabase::class) {
                    var dbBuilder = Room
                            .databaseBuilder(context, GymAssistantDatabase::class.java, "gymAssistantDb")
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