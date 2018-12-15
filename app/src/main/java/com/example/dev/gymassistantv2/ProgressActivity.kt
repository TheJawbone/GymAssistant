package com.example.dev.gymassistantv2

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.example.dev.gymassistantv2.R.id.chart
import com.github.mikephil.charting.data.*


class ProgressActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        setNavigationControls()
        setCategorySpinner()
        setMeasurementsSpinner()
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
                    0 -> dbContext!!.muscleGroupDao().getAllNames()
                    1 -> dbContext!!.exerciseDao().getAllNames()
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
                        val measurements = dbContext!!.measurementDao().getForBodyPart(bodyPart.id!!)
                        val valuesX = mutableListOf<Int>()
                        val valuesY = mutableListOf<Int>()
                        var counter = 0
                        measurements.forEach {
                            valuesX.add(counter)
                            valuesY.add(it.value!!)
                            counter++
                        }
                        setChart(valuesX, valuesY, selectedItem)
                    }
                    1 -> {

                    }
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setChart(valuesX: List<Int>, valuesY: List<Int>, label: String) {
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
}