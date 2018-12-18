package com.example.dev.gymassistantv2.Database

import com.example.dev.gymassistantv2.Entities.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class DBInitializer(private val dbContext : GymAssistantDatabase?) {

    private val firstNames = Arrays.asList("Marek", "Konrad", "Andrew", "Robert", "Patryk", "Marta", "Luiza", "Julia")
    private val lastNames = Arrays.asList("Kondratowicz", "Lubicz", "Wołoń", "Kulski", "Drawski", "Grabik", "Ustańczyk", "Bzieliński")

    fun populateMuscleGroup() {
        if(dbContext!!.muscleGroupDao().getAll().isEmpty()) {
            val muscleGroups = mutableListOf<MuscleGroup>()
            val muscleGroupNames = listOf("Barki", "Biceps", "Triceps", "Klatka piersiowa",
                    "Grzbiet", "Brzuch", "Uda", "Łydki")
            muscleGroupNames.forEach {
                muscleGroups.add(MuscleGroup(null, it))
            }
            dbContext.muscleGroupDao().insert(muscleGroups)
        }
    }

    fun populateExercise() {
        if(dbContext!!.exerciseDao().getDefault().isEmpty()) {
            val exercises = mutableListOf<Exercise>()

            val exercise1 = Exercise()
            exercise1.name = "Wyciskanie sztangi nad głową"
            exercise1.defaultExercise = 1
            var muscleGroup = dbContext.muscleGroupDao().getByName("Barki")
            exercise1.muscleGroupId = muscleGroup.id
            exercises.add(exercise1)

            val exercise2 = Exercise()
            exercise2.name = "Podnoszenie hantli w uchwycie młotkowym"
            exercise2.defaultExercise = 1
            muscleGroup = dbContext.muscleGroupDao().getByName("Biceps")
            exercise2.muscleGroupId = muscleGroup.id
            exercises.add(exercise2)

            dbContext.exerciseDao().insert(exercises)
        }
    }

    fun populateTrainers() {
        if(dbContext!!.userDao().getAllTrainers().isEmpty()) {
            for (i in 1..5) {
                dbContext.userDao().insert(User(Random().nextLong(), true, firstNames[Random().nextInt(firstNames.size)], lastNames[Random().nextInt(lastNames.size)] ))
            }
        }
    }

    fun populateChargesForTrainer(trainerId : Long) {
        if(dbContext!!.userDao().getChargesForUser(trainerId).isNotEmpty()) return
        for (usersCount in 1..12) {
            val userId = dbContext.userDao().insert(User(Random().nextLong(), false, trainerId, firstNames[Random().nextInt(firstNames.size)], lastNames[Random().nextInt(lastNames.size)]))
            generateWorkoutHistoryForUser(userId)
        }
    }

    fun generateWorkoutHistoryForUser(userId: Long) {
        if(dbContext!!.workoutDao().getForUser(userId).isNotEmpty()) return
        for (workoutsCount in 1..Random().nextInt(10) + 1) {
            val exampleWorkout = Workout()
            exampleWorkout.date = System.currentTimeMillis().minus(ThreadLocalRandom.current().nextLong(5000000000))
            exampleWorkout.userId = userId

            val exampleWorkoutId = dbContext.workoutDao().insert(exampleWorkout)

            for (segmentsCount in 1..Random().nextInt(10) + 1) {
                val exampleSegment = Segment()
                exampleSegment.workoutId = exampleWorkoutId
                exampleSegment.exerciseId = Random().nextInt(1) + 1.toLong()

                val exampleSegmentId = dbContext.segmentDao().insert(exampleSegment)

                for (setsCount in 1..Random().nextInt(10) + 1) {
                    val exampleSet = ExerciseSet()
                    exampleSet.repCount = Random().nextInt(30) + 10
                    exampleSet.weight = Random().nextInt(20) + 10
                    exampleSet.segmentId = exampleSegmentId

                    dbContext.exerciseSetDao().insert(exampleSet)
                }
            }
        }
    }
}