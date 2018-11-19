package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainMenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val buttonBeginWorkout = findViewById<Button>(R.id.buttonBeginWorkout)
        val intent = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginWorkout.setOnClickListener { startActivity(intent) }
    }
}
