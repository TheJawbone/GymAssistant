package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase

class ManageChargesActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_charges)

        processIntent()
        setBackButton()
        generateChargesList(setLayout())
    }

    private fun setLayout(): LinearLayout {
        return findViewById<LinearLayout>(R.id.chargesLinearLayout)!!
    }

    private fun setBackButton() {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { finish() }
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun generateChargesList(layout: LinearLayout) {
        layout.removeAllViews()
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val charges = dbContext!!.userDao().getChargesForUser(loggedUser.userId!!)


        if(charges.isEmpty()) {
            Toast.makeText(this, "Brak podopiecznych!", Toast.LENGTH_LONG).show()
        }

        charges.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL

            val chargeId = it.id!!
            val chargeFirstName = it.firstName!!
            val chargeLastName = it.lastName!!
            val buttonText = "ID: $chargeId $chargeFirstName $chargeLastName"
            val buttonMain = Button(this)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
            buttonMain.text = buttonText
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            buttonMain.typeface = typeface

            buttonMain.setOnClickListener {
                val intent = Intent(this, ProgressActivity::class.java)
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
                generateChargesList(layout)
            }

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}