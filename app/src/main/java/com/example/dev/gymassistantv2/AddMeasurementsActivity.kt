package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import com.example.dev.gymassistantv2.Entities.Measurement

class AddMeasurementsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_measurements)

        setNavigationControls()
        setBodyPartSpinner()
    }

    private fun setNavigationControls() {
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val intentSave = Intent(this, ProgressSubmenuActivity::class.java)
        buttonSave.setOnClickListener {
            val dbContext = GymAssistantDatabase.getInstance(this)
            val bodyPart = dbContext!!.muscleGroupDao().getByName(
                    findViewById<Spinner>(R.id.spinnerMuscleGroups).selectedItem.toString()
            )
            val measurement = Measurement()
            measurement.value = findViewById<EditText>(R.id.editTextMeasurement).text.toString().toInt()
            measurement.bodyPartId = bodyPart.id
            dbContext!!.measurementDao().insert(measurement)
            Toast.makeText(this, "Pomyślnie dodano pomiar", Toast.LENGTH_LONG).show()
            startActivity(intentSave)
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { super.onBackPressed() }
    }

    private fun setBodyPartSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                GymAssistantDatabase.getInstance(this)!!.muscleGroupDao().getAllNames())
        spinner.adapter = adapter
    }
}