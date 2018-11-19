package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class ChooseExerciseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_excercise)

        val buttonBeginSet = findViewById<Button>(R.id.buttonBeginSet)
        val intent = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginSet.setOnClickListener { startActivity(intent) }
    }
}
