package com.example.dev.gymassistantv2.database

import com.example.dev.gymassistantv2.R.string.exercises
import com.example.dev.gymassistantv2.entities.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class DBInitializer(private val dbContext : GymAssistantDatabase?) {

    class DefaultExercise constructor(var name: String, var muscleGroup: String)

    private val defaultExercises: MutableList<DefaultExercise> =
            mutableListOf(
                    DefaultExercise("Wznosy ramion w opadzie tułowia", "Barki"),
                    DefaultExercise("Wznosy ramion na boki przy wyciągu", "Barki"),
                    DefaultExercise("Unoszenie ramienia w bok stojąc", "Barki"),


                    DefaultExercise("Unoszenie ramion na modlitewniku", "Biceps"),
                    DefaultExercise("Unoszenie przedramienia jednorącz", "Biceps"),
                    DefaultExercise(" Unoszenie sztangielek stojąc", "Biceps"),


                    DefaultExercise("Wyciskanie sztangielki jednorącz zza karku", "Triceps"),
                    DefaultExercise("Wyciskanie sztangi w wąskim uchwycie", "Triceps"),
                    DefaultExercise("Uginanie ramion w leżeniu na ławce", "Triceps"),

                    DefaultExercise("Wyciskanie w leżeniu na ławce skośnej", "Klatka piersiowa"),
                    DefaultExercise("Pompki na poręczach", "Klatka piersiowa"),
                    DefaultExercise("Rozpiętki na ławce poziomej", "Klatka piersiowa"),


                    DefaultExercise("Unoszenie sztangi do klatki piersiowej", "Plecy"),
                    DefaultExercise("Przenoszenie sztangielki za głowę", "Plecy"),
                    DefaultExercise("Unoszenie sztangielki jednorącz", "Plecy"),


                    DefaultExercise("Brzuszki z obciążeniem", "Brzuch"),
                    DefaultExercise("Unoszenie bioder", "Brzuch"),
                    DefaultExercise("Przenoszenie piłki", "Brzuch"),

                    DefaultExercise("Przysiady ze sztangą na czworobocznych", "Uda"),
                    DefaultExercise("Wykroki", "Uda"),
                    DefaultExercise("Zginanie nóg na maszynie siedząc", "Uda"),

                    DefaultExercise("Wspięcia na palce", "Łydki"),
                    DefaultExercise("Wspięcia na palcach stojąc z wykorzystaniem suwnicy Smitha", "Łydki"),
                    DefaultExercise("Wspięcia na palcach siedząc na maszynie", "Łydki")
                    )

    fun populateMuscleGroup() {
        if(dbContext!!.muscleGroupDao().getAll().isEmpty()) {
            val muscleGroups = mutableListOf<MuscleGroup>()
            val muscleGroupNames = listOf("Barki", "Biceps", "Triceps", "Klatka piersiowa",
                    "Plecy", "Brzuch", "Uda", "Łydki")
            muscleGroupNames.forEach {
                muscleGroups.add(MuscleGroup(null, it))
            }
            dbContext.muscleGroupDao().insert(muscleGroups)
        }
    }

    fun populateDefaultExercisesForUser(userId: Long) {
        if(dbContext!!.exerciseDao().getDefault().isEmpty()) {

            for(defaultExerciseIndex in 0 until defaultExercises.size) {
                val exercise = Exercise()
                exercise.name = defaultExercises[defaultExerciseIndex].name
                exercise.defaultExercise = 1
                val muscleGroup = dbContext.muscleGroupDao().getByName(defaultExercises[defaultExerciseIndex].muscleGroup)
                exercise.muscleGroupId = muscleGroup.id
                exercise.ownerId = userId

                dbContext.exerciseDao().insert(exercise)
            }
        }
    }
}