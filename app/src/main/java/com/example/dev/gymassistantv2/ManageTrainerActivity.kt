package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ManageTrainerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_trainer)

        val trainerPresent = false // TODO: check in database if trainer assigned to the user
        val invitationPending = false // TODO: check if there is a pending invitation
        val buttonFindTrainer = findViewById<Button>(R.id.buttonFindOrDeleteTrainer)
        val textViewTrainerData = findViewById<TextView>(R.id.textViewTrainerData)
        if (!trainerPresent) {
            if(invitationPending) {
                buttonFindTrainer.text = resources.getString(R.string.delete_invitation)
                textViewTrainerData.text = resources.getString(R.string.invitation_pending)
            }
            else {
                buttonFindTrainer.text = resources.getString(R.string.find_trainer)
                textViewTrainerData.text = resources.getString(R.string.no_trainer)
                val intentFindTrainer = Intent(this, FindTrainerActivity::class.java)
                buttonFindTrainer.setOnClickListener { startActivity(intentFindTrainer) }
            }
        }
        else {
            buttonFindTrainer.text = resources.getString(R.string.find_trainer)
        }
    }
}