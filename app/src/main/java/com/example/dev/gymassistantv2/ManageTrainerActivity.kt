package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.dev.gymassistantv2.DTOs.PendingInvitationDto
import com.example.dev.gymassistantv2.DTOs.UserDto
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase

class ManageTrainerActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var pendingInvitation : PendingInvitationDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_trainer)

        determineLoggedUserData()
        setPendingInvitation()
        setContent()
        setBackButton()
    }

    private fun setContent() {
        val buttonFindTrainer = findViewById<Button>(R.id.buttonFindTrainer)
        val textViewTrainerData = findViewById<TextView>(R.id.textViewTrainerData)
        if (isTrainerNotAssignedToUser()) {
            if (isInvitationPending()) {
                buttonFindTrainer.text = "Show invitation"
                textViewTrainerData.text = resources.getString(R.string.invitation_pending)
                val intentFindTrainer = Intent(this, FindTrainerActivity::class.java)
                intentFindTrainer.putExtra("pendingInvitation", pendingInvitation)
                intentFindTrainer.putExtra("loggedUser", loggedUser)
                buttonFindTrainer.setOnClickListener { startActivity(intentFindTrainer) }
            } else {
                buttonFindTrainer.text = resources.getString(R.string.find_trainer)
                textViewTrainerData.text = resources.getString(R.string.no_trainer)
                val intentFindTrainer = Intent(this, FindTrainerActivity::class.java)
                intentFindTrainer.putExtra("pendingInvitation", pendingInvitation)
                intentFindTrainer.putExtra("loggedUser", loggedUser)
                buttonFindTrainer.setOnClickListener { startActivity(intentFindTrainer) }
            }
        } else {
            buttonFindTrainer.text = resources.getString(R.string.find_trainer)
            buttonFindTrainer.isEnabled = false
        }
    }

    private fun isInvitationPending() = pendingInvitation!!.id != null

    private fun isTrainerNotAssignedToUser() = loggedUser.trainerId == null

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setPendingInvitation() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val existingInvitation = dbContext!!.invitationDao().getForSender(loggedUser.userId!!)
        pendingInvitation = if(existingInvitation != null)
            PendingInvitationDto(existingInvitation)
        else
            PendingInvitationDto()
    }

    private fun setBackButton() {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            val intentMainMenu = Intent(this, MainMenuActivity::class.java)
            intentMainMenu.putExtra("loggedUser", loggedUser)
            startActivity(intentMainMenu)
        }
    }
}