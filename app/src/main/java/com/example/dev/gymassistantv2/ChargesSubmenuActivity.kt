package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.dev.gymassistantv2.dtos.UserDto

class ChargesSubmenuActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charges_submenu)

        determineLoggedUserData()
        setManageChargesButton()
        setManageInvitationsButton()
        setBackButton()
    }

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setManageChargesButton() {
        val buttonManageCharges = findViewById<Button>(R.id.buttonManageCharges)
        val intentManageCharges = Intent(this, ManageChargesActivity::class.java)
        buttonManageCharges.setOnClickListener {
            intentManageCharges.putExtra("loggedUser", loggedUser)
            startActivity(intentManageCharges)
        }
    }

    private fun setManageInvitationsButton() {
        val buttonManageInvitations = findViewById<Button>(R.id.buttonManageInvitations)
        val intentManageInvitations = Intent(this, ManageInvitationsActivity::class.java)
        buttonManageInvitations.setOnClickListener {
            intentManageInvitations.putExtra("loggedUser", loggedUser)
            startActivity(intentManageInvitations)
        }
    }

    private fun setBackButton() {
        val buttonMainMenu = findViewById<Button>(R.id.buttonBack)
        buttonMainMenu.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
