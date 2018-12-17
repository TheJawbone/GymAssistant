package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.dev.gymassistantv2.DTOs.UserDto

class ChargesSubmenuActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charges_submenu)

        determineLoggedUserData()
        setManageChargesButton()
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

    private fun setBackButton() {
        val buttonMainMenu = findViewById<Button>(R.id.buttonBack)
        val intentManageCharges = Intent(this, MainMenuActivity::class.java)
        buttonMainMenu.setOnClickListener {
            intentManageCharges.putExtra("loggedUser", loggedUser)
            startActivity(intentManageCharges)
        }
    }

    override fun onBackPressed() {
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        intentMainMenu.putExtra("loggedUser", loggedUser)
        startActivity(intentMainMenu)
    }
}
