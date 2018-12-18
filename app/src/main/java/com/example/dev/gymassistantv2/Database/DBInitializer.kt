package com.example.dev.gymassistantv2.Database

import com.example.dev.gymassistantv2.Entities.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class DBInitializer(private val dbContext : GymAssistantDatabase?) {

    private val williamsFbId = 109931923389001
    private val williamsId = 1.toLong()

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
        if(dbContext!!.exerciseDao().getDefault().isEmpty()) {
            var exercises = mutableListOf<Exercise>()

            var exercise1 = Exercise()
            exercise1.name = "Wyciskanie sztangi nad głową"
            exercise1.defaultExercise = 1
            var muscleGroup = dbContext!!.muscleGroupDao().getByName("Barki")
            exercise1.muscleGroupId = muscleGroup.id
            exercises.add(exercise1)

            var exercise2 = Exercise()
            exercise2.name = "Podnoszenie hantli w uchwycie młotkowym"
            exercise2.defaultExercise = 1
            muscleGroup = dbContext!!.muscleGroupDao().getByName("Biceps")
            exercise2.muscleGroupId = muscleGroup.id
            exercises.add(exercise2)

            dbContext.exerciseDao().insert(exercises)
        }
    }

    fun populateTrainers() {
        if(dbContext!!.userDao().getAllTrainers().isEmpty()) {
            for (i in 1..5) {
                dbContext.userDao().insert(User(Random().nextLong(), true))
            }
        }
    }

    fun populateChargesForWilliam() {
        if(dbContext!!.userDao().getChargesForUser(williamsId).isEmpty()) {
            for (i in 1..12) {
                val facebookId = Random().nextLong()
                dbContext.userDao().insert(User(facebookId, false, williamsId))

                val exampleWorkout = Workout()
                exampleWorkout.date = System.currentTimeMillis()
                exampleWorkout.userId =  dbContext.userDao().getByFacebookId(facebookId).id

                val exampleWorkoutId = dbContext.workoutDao().insert(exampleWorkout)

                val exampleSegment = Segment()
                exampleSegment.workoutId = exampleWorkoutId
                exampleSegment.exerciseId = 1

                val exampleSegmentId = dbContext.segmentDao().insert(exampleSegment)

                val exampleSetOne = ExerciseSet()
                exampleSetOne.repCount = 10
                exampleSetOne.weight = 25
                exampleSetOne.segmentId = exampleSegmentId

                val exampleSetTwo = ExerciseSet()
                exampleSetTwo.repCount = 15
                exampleSetTwo.weight = 20
                exampleSetTwo.segmentId = exampleSegmentId

                dbContext.exerciseSetDao().insert(exampleSetOne)
                dbContext.exerciseSetDao().insert(exampleSetTwo)


                val secondExampleSegment = Segment()
                secondExampleSegment.workoutId = exampleWorkoutId
                secondExampleSegment.exerciseId = 2

                val secondExampleSegmentId = dbContext.segmentDao().insert(secondExampleSegment)

                val secondExampleSetOne = ExerciseSet()
                secondExampleSetOne.repCount = 12
                secondExampleSetOne.weight = 40
                secondExampleSetOne.segmentId = secondExampleSegmentId

                val secondExampleSetTwo = ExerciseSet()
                secondExampleSetTwo.repCount = 17
                secondExampleSetTwo.weight = 45
                secondExampleSetTwo.segmentId = secondExampleSegmentId

                dbContext.exerciseSetDao().insert(secondExampleSetOne)
                dbContext.exerciseSetDao().insert(secondExampleSetTwo)

            }
        }
    }
}