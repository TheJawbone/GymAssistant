package com.example.dev.gymassistantv2.Database

import com.example.dev.gymassistantv2.DAOs.MuscleGroupDAO
import com.example.dev.gymassistantv2.Entities.Exercise
import com.example.dev.gymassistantv2.Entities.MuscleGroup

class DBInitializer(private val dbContext : GymAssistantDatabase?) {

    fun populateMuscleGroup() {
        if(dbContext!!.muscleGroupDao().getAll().isEmpty()) {
            var muscleGroups = mutableListOf<MuscleGroup>()
            var muscleGroupNames = listOf("Barki", "Biceps", "Triceps", "Klatka piersiowa",
                    "Grzbiet", "Brzuch", "Uda", "Łydki")
            muscleGroupNames.forEach {
                muscleGroups.add(MuscleGroup(null, it))
            }
            dbContext.muscleGroupDao().insert(muscleGroups)
        }
    }

    fun populateExercise() {
        dbContext!!.exerciseDao().deleteAll()
        var defaultExercises = dbContext!!.exerciseDao().getAll()
        if(dbContext!!.exerciseDao().getDefault().isEmpty()) {
            var exercises = mutableListOf<Exercise>()
            var muscleGroup = MuscleGroup()

            var exercise1 = Exercise()
            exercise1.name = "Wyciskanie sztangi nad głową"
            muscleGroup = dbContext!!.muscleGroupDao().getByName("Barki")
            exercise1.muscleGroupId = muscleGroup.id
            exercises.add(exercise1)

            var exercise2 = Exercise()
            exercise2.name = "Podnoszenie hantli w uchwycie młotkowym"
            muscleGroup = dbContext!!.muscleGroupDao().getByName("Biceps")
            exercise2.muscleGroupId = muscleGroup.id
            exercises.add(exercise2)

            dbContext.exerciseDao().insert(exercises)
        }
    }
}