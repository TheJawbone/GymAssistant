package com.example.dev.gymassistantv2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.Invitation
import com.example.dev.gymassistantv2.entities.User

class ManageInvitationsActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_invitations)

        determineLoggedUserData()
        generateInvitationList()
        setBackButton()
    }

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    @SuppressLint("SetTextI18n")
    private fun generateInvitationList() {
        //val scrollView = findViewById<ScrollView>(R.id.scrollView)
        //val header = findViewById<TextView>(R.id.textViewHeader)
        //header.text = "Heyy"

        /*layout.setPadding(Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0,
                Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0)*/
        //scrollView.addView(layout)



        val layout = findViewById<LinearLayout>(R.id.linearLayout)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)


        val dbContext = GymAssistantDatabase.getInstance(this)
        val segments = dbContext!!.invitationDao().getForRecipient(loggedUser.userId!!)

        if(segments.isEmpty())
            Toast.makeText(this, "Brak zaproszeń", Toast.LENGTH_LONG).show()


        segments.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.gravity = Gravity.CENTER_VERTICAL

            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            val sender = dbContext.userDao().getById(it.senderId!!)

            val buttonMain = setMainButton(metrics, typeface, sender)
            val buttonAccept = setAcceptButton(buttonMain, metrics, typeface, it, sender, dbContext)
            val buttonDelete = setDeleteButton(buttonMain, metrics, typeface, it,  dbContext)

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonAccept)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMainButton(metrics: DisplayMetrics, typeface: Typeface?, sender: User): Button {
        val buttonMain = Button(applicationContext)
        buttonMain.layoutParams = LinearLayout.LayoutParams(
                (metrics.widthPixels * 0.6).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
        buttonMain.text = "From: ${sender.firstName} ${sender.lastName}"
        buttonMain.typeface = typeface
        buttonMain.measure(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        return buttonMain
    }

    @SuppressLint("SetTextI18n")
    private fun setAcceptButton(buttonMain: Button, metrics: DisplayMetrics, typeface: Typeface?, invitation: Invitation, newCharge: User, dbContext: GymAssistantDatabase): Button {
        val buttonAccept = Button(this)
        buttonAccept.height = buttonMain.measuredHeight
        buttonAccept.width = (metrics.widthPixels * 0.2).toInt()
        buttonAccept.background = applicationContext.getDrawable(R.drawable.bottom_border_blue)
        buttonAccept.text = "Akceptuj"
        buttonAccept.typeface = typeface
        buttonAccept.setOnClickListener {
            newCharge.trainerId = loggedUser.userId
            dbContext.userDao().update(newCharge)
            dbContext.invitationDao().delete(invitation)
            refreshActivity()
        }
        return buttonAccept
    }

    @SuppressLint("SetTextI18n")
    private fun setDeleteButton(buttonMain: Button, metrics: DisplayMetrics, typeface: Typeface?, invitation: Invitation, dbContext: GymAssistantDatabase): Button {
        val buttonDelete = Button(this)
        buttonDelete.height = buttonMain.measuredHeight
        buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
        buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
        buttonDelete.text = "Usuń"
        buttonDelete.typeface = typeface
        buttonDelete.setOnClickListener {
            dbContext.invitationDao().delete(invitation)
            refreshActivity()
        }
        return buttonDelete
    }

    private fun setBackButton() {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun refreshActivity() {
        val intent = Intent(this, ManageInvitationsActivity::class.java)
        intent.putExtra("loggedUser", loggedUser)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChargesSubmenuActivity::class.java)
        intent.putExtra("loggedUser", loggedUser)
        startActivity(intent)
    }

}