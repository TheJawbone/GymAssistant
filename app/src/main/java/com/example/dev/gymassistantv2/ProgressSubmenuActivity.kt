package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.dev.gymassistantv2.DTOs.UserDto

class ProgressSubmenuActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_submenu)

        processIntent()
        setNavigationControls()
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setNavigationControls() {
        val buttonProgress = findViewById<Button>(R.id.buttonCheckProgress)
        val intentProgress = Intent(this, ProgressActivity::class.java)
        buttonProgress.setOnClickListener {
            intentProgress.putExtra("loggedUser", loggedUser)
            startActivity(intentProgress)
        }

        val buttonAddMeasurement = findViewById<Button>(R.id.buttonAddMeasurements)
        val intentAddMeasurements = Intent(this, AddMeasurementsActivity::class.java)
        buttonAddMeasurement.setOnClickListener {
            intentAddMeasurements.putExtra("loggedUser", loggedUser)
            startActivity(intentAddMeasurements)
        }

        val buttonManageMeasurements = findViewById<Button>(R.id.buttonManageMeasurements)
        val intentManageMeasurements = Intent(this, ManageMeasurementsActivity::class.java)
        buttonManageMeasurements.setOnClickListener {
            intentManageMeasurements.putExtra("loggedUser", loggedUser)
            startActivity(intentManageMeasurements)
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { super.onBackPressed() }
    }
}