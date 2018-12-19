package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import android.widget.AdapterView
import android.content.res.ColorStateList
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.graphics.Typeface
import android.view.View
import com.example.dev.gymassistantv2.database.GymAssistantDatabase


class ChooseAvailableExercisesActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //Call base class constructor
        super.onCreate(savedInstanceState)

        //ExerciseSet content view
        setContentView(R.layout.activity_choose_available_exercises)

        //ExerciseSet fonts
        val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
        var textView = findViewById<TextView>(R.id.textViewMuscleGroups)
        textView.typeface = typeface
        textView = findViewById(R.id.textViewSort)
        textView.typeface = typeface

        //Set up muscle group spinner
        val db = GymAssistantDatabase.getInstance(applicationContext)
        val muscleGroups = db!!.muscleGroupDao().getAll()
        var spinnerArray = arrayOfNulls<String>(muscleGroups.size)
        var i = 0
        for (muscleGroup in muscleGroups) {
            spinnerArray[i] = muscleGroup.name
            i++
        }
        val spinnerMuscleGroup = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMuscleGroup.adapter = adapter
        spinnerMuscleGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSpinners()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Set up sort spinner
        spinnerArray = arrayOf(getString(R.string.a_z), getString(R.string.z_a))
        val spinnerSort = findViewById<Spinner>(R.id.spinnerSort)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray)
        spinnerSort.adapter = adapter
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSpinners()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateSpinners() {
        var spinner = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val muscleGroupName = spinner.selectedItem.toString()
        spinner = findViewById(R.id.spinnerSort)
        val sortValue = spinner.selectedItem.toString()

        val db = GymAssistantDatabase.getInstance(applicationContext)
        val muscleGroup = db!!.muscleGroupDao().getByName(muscleGroupName)
        var exerciseList = db!!.exerciseDao().getForMuscleGroup(muscleGroup.id!!)
        if (sortValue == "Z-A") {
            exerciseList = exerciseList.reversed()
        }
        val layout = findViewById<LinearLayout>(R.id.linearLayout)
        layout.removeAllViews()
        for (exercise in exerciseList) {
            val checkBox = CheckBox(applicationContext)
            checkBox.text = exercise.name
            checkBox.background = applicationContext.getDrawable(R.drawable.bottom_border)
            checkBox.isChecked = exercise.visible!!
            checkBox.buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            checkBox.id = exercise.id!!.toInt()
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                run {
                    val exercise = db.exerciseDao().getById(buttonView.id.toLong())
                    exercise.visible = isChecked
                    db.exerciseDao().update(exercise)
                }
            }
            layout.addView(checkBox)
        }
    }
}