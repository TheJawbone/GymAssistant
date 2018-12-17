package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.*
import com.example.dev.gymassistantv2.DTOs.UserDto
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase

class ManageChargesActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_charges)

        processIntent()
        setBackButton()
        generateChargesList()
    }

    private fun setBackButton() {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { goToChargesSubmenuActivity() }
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun generateChargesList() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val layout = findViewById<LinearLayout>(R.id.chargesLinearLayout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val charges = dbContext!!.userDao().getChargesForUser(loggedUser.userId!!)


        if(charges.isEmpty()) {
            Toast.makeText(this, "Brak podopiecznych!", Toast.LENGTH_LONG).show()
        }

        charges.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL

            val chargeId = it.id!!
            val buttonText = "ID: $chargeId"
            val buttonMain = Button(this)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
            buttonMain.text = buttonText
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            buttonMain.typeface = typeface

            buttonMain.setOnClickListener {
                val intent = Intent(this, WorkoutHistoryActivity::class.java)
                intent.putExtra("loggedUser", loggedUser)
                intent.putExtra("historyOwnerId", chargeId)
                startActivity(intent)
            }

            val buttonDelete = Button(this)
            buttonDelete.height = buttonMain.height
            buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
            buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
            buttonDelete.text = "Usuń"
            buttonDelete.typeface = typeface
            buttonDelete.setOnClickListener {
                val charge = dbContext.userDao().getById(chargeId)
                charge.trainerId = null
                dbContext.userDao().update(charge)
                val intent = Intent(this, ManageChargesActivity::class.java)
                intent.putExtra("loggedUser", loggedUser)
                startActivity(intent)
            }

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }

    override fun onBackPressed() {
        goToChargesSubmenuActivity()
    }

    private fun goToChargesSubmenuActivity() {
        val intentChargesSubmenu = Intent(this, ChargesSubmenuActivity::class.java)
        intentChargesSubmenu.putExtra("loggedUser", loggedUser)
        startActivity(intentChargesSubmenu)
    }


}