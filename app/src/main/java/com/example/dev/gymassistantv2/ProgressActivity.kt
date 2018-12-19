package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.ExerciseSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*


class ProgressActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var historyOwnerId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        processIntent()
        setNavigationControls()
        setCategorySpinner()
        setMeasurementsSpinner()
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        this.historyOwnerId = this.intent.getLongExtra("historyOwnerId", historyOwnerId)
    }

    private fun setNavigationControls() {
        findViewById<Button>(R.id.buttonBack).setOnClickListener { super.onBackPressed() }
    }

    private fun setCategorySpinner() {
        findViewById<Spinner>(R.id.spinnerCategory).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val dbContext = GymAssistantDatabase.getInstance(applicationContext)
                val spinner = findViewById<Spinner>(R.id.spinnerMeasurements)
                var spinnerItems = when (findViewById<Spinner>(R.id.spinnerCategory).selectedItemPosition) {
                    0 -> dbContext!!.muscleGroupDao().getAllNames().sorted()
                    1 -> dbContext!!.exerciseDao().getAllNames().sorted()
                    else -> null
                }
                var adapter = ArrayAdapter<String>(applicationContext,
                        android.R.layout.simple_spinner_item, spinnerItems)
                spinner.adapter = adapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setMeasurementsSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerMeasurements)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val dbContext = GymAssistantDatabase.getInstance(applicationContext)
                val selectedItem = spinner.selectedItem.toString()
                when (findViewById<Spinner>(R.id.spinnerCategory).selectedItemPosition) {
                    0 -> {
                        val bodyPart = dbContext!!.muscleGroupDao().getByName(selectedItem)
                        val measurements = dbContext.measurementDao().getForBodyPartAndUser(bodyPart.id!!, historyOwnerId)
                        val valuesX = mutableListOf<Int>()
                        val valuesY = mutableListOf<Int>()
                        var counter = 0
                        measurements.forEach {
                            valuesX.add(counter)
                            valuesY.add(it.value!!)
                            counter++
                        }
                        setMeasurementsChart(valuesX, valuesY, selectedItem)
                    }
                    1 -> {
                        val exercise = dbContext!!.exerciseDao().getByName(selectedItem)
                        val workouts = dbContext!!.workoutDao().getAll()
                        val sets = mutableListOf<ExerciseSet>()
                        workouts.forEach {
                            val segments = dbContext!!.segmentDao().getSpecificForWorkout(it.id!!, exercise.id!!)
                            segments.forEach {
                                val segmentSets = dbContext!!.exerciseSetDao().getForSegment(it.id!!)
                                segmentSets.forEach {
                                    sets.add(it)
                                }
                            }
                        }
                        val valuesX = mutableListOf<Int>()
                        val weightsY = mutableListOf<Int>()
                        val repsY = mutableListOf<Int>()
                        var counter = 0
                        sets.forEach {
                            valuesX.add(counter)
                            weightsY.add(it.weight!!)
                            repsY.add(it.repCount!!)
                            counter++
                        }
                        setExercisesChart(valuesX, weightsY, repsY, selectedItem)
                    }
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setMeasurementsChart(valuesX: List<Int>, valuesY: List<Int>, label: String) {
        val chart = findViewById<View>(R.id.chart) as BarChart
        val entries = ArrayList<BarEntry>()

        for(i in 0 until valuesX.count()) {
            entries.add(BarEntry(valuesX[i].toFloat(), valuesY[i].toFloat()))
        }

        val dataSet = BarDataSet(entries, label)
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLUE

        val barData = BarData(dataSet)
        chart.data = barData
        chart.invalidate()
    }

    private fun setExercisesChart(valuesX: List<Int>, weightsY: List<Int>, repsY: List<Int>, label: String) {
        val chart = findViewById<View>(R.id.chart) as BarChart
        val weightEntries = ArrayList<BarEntry>()
        val repEntries = ArrayList<BarEntry>()

        for(i in 0 until valuesX.count()) {
            weightEntries.add(BarEntry(valuesX[i].toFloat(), weightsY[i].toFloat()))
            repEntries.add(BarEntry(valuesX[i].toFloat(), repsY[i].toFloat()))
        }

        val weightsDataSet = BarDataSet(weightEntries, "Obciązenie")
        weightsDataSet.color = Color.RED
        weightsDataSet.valueTextColor = Color.RED

        val repsDataSet = BarDataSet(repEntries, "Powtórzenia")
        weightsDataSet.color = Color.BLUE
        weightsDataSet.valueTextColor = Color.BLUE

        val barData = BarData(listOf(weightsDataSet, repsDataSet))
        chart.data = barData
        chart.invalidate()
    }

    override fun onBackPressed() {
        finish()
    }
}