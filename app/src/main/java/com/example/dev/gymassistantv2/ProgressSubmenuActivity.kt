package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ProgressSubmenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_submenu)

        setNavigationControls()
    }

    private fun setNavigationControls() {
        val buttonProgress = findViewById<Button>(R.id.buttonCheckProgress)
        val intentProgress = Intent(this, ProgressActivity::class.java)
        buttonProgress.setOnClickListener { startActivity(intentProgress) }

        val buttonAddMeasurement = findViewById<Button>(R.id.buttonAddMeasurements)
        val intentAddMeasurements = Intent(this, AddMeasurementsActivity::class.java)
        buttonAddMeasurement.setOnClickListener { startActivity(intentAddMeasurements) }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { super.onBackPressed() }
    }
}