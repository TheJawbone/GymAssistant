package com.example.dev.gymassistantv2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import com.example.dev.gymassistantv2.dtos.PendingInvitationDto
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.Invitation
import com.example.dev.gymassistantv2.entities.User

class FindTrainerActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var pendingInvitation : PendingInvitationDto? = null
    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_trainer)

        processIntent()
        setLayout()
        setBackButton()
    }

    override fun onResume() {
        super.onResume()
        setContent()
    }

    private fun setContent() {
        layout.removeAllViews()
        setTitleDescription()
        when {
            isTrainerAssignedToUser() -> showAssignedTrainer()
            isInvitationPending() -> showPendingInvitation()
            else -> generateAvailableTrainerList()
        }
    }

    private fun setLayout() {
        layout = findViewById(R.id.linearLayout)
    }

    private fun showAssignedTrainer() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dbContext = GymAssistantDatabase.getInstance(this)

        val horizontalLayout = LinearLayout(this)
        horizontalLayout.orientation = LinearLayout.HORIZONTAL
        horizontalLayout.gravity = Gravity.CENTER_VERTICAL

        val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
        val trainer = dbContext!!.userDao().getById(loggedUser.trainerId!!)

        val buttonMain = setMainButton(metrics, typeface, trainer)
        val buttonDelete = setDeleteButton(buttonMain, metrics, typeface, dbContext)

        horizontalLayout.addView(buttonMain)
        horizontalLayout.addView(buttonDelete)
        layout.addView(horizontalLayout)
    }

    private fun isTrainerAssignedToUser() = loggedUser.trainerId != null


    private fun setTitleDescription() {
        val title = findViewById<TextView>(R.id.textViewDescription)
        if(loggedUser.trainerId != null)
            title.text = "Twój trener"
        else if(isInvitationPending())
            title.text = "Wysłane zaproszenie"
    }

    private fun processIntent() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        pendingInvitation = this.intent.getSerializableExtra("pendingInvitation") as PendingInvitationDto
    }

    private fun showPendingInvitation() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dbContext = GymAssistantDatabase.getInstance(this)

        val horizontalLayout = LinearLayout(this)
        horizontalLayout.orientation = LinearLayout.HORIZONTAL
        horizontalLayout.gravity = Gravity.CENTER_VERTICAL

        val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
        val trainer = dbContext!!.userDao().getById(pendingInvitation!!.recipientId!!)

        val buttonMain = setMainButton(metrics, typeface, trainer)
        val buttonInvite = setInviteButton(buttonMain, metrics, typeface, dbContext, trainer)
        val buttonDelete = setDeleteButton(buttonMain, metrics, typeface, dbContext)

        horizontalLayout.addView(buttonMain)
        horizontalLayout.addView(buttonInvite)
        horizontalLayout.addView(buttonDelete)
        layout.addView(horizontalLayout)
    }

    @SuppressLint("SetTextI18n")
    private fun generateAvailableTrainerList() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val trainers = dbContext!!.userDao().getAllTrainers()

        if(trainers.isEmpty())
            Toast.makeText(this, "Brak dostępnych trenerów!", Toast.LENGTH_LONG).show()


        trainers.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.gravity = Gravity.CENTER_VERTICAL

            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")

            val buttonMain = setMainButton(metrics, typeface, it)
            val buttonInvite = setInviteButton(buttonMain, metrics, typeface, dbContext, it)

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonInvite)
            layout.addView(horizontalLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMainButton(metrics: DisplayMetrics, typeface: Typeface?, trainer: User): Button {
        val buttonMain = Button(this)
        if(pendingInvitation!!.id != null)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.6).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        else
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonMain.background = this.getDrawable(R.drawable.bottom_border)
        buttonMain.text = "${trainer.firstName} ${trainer.lastName}, id: ${trainer.id}"
        buttonMain.typeface = typeface
        buttonMain.measure(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        return buttonMain
    }

    @SuppressLint("SetTextI18n")
    private fun setInviteButton(buttonMain: Button, metrics: DisplayMetrics, typeface: Typeface?, dbContext: GymAssistantDatabase?, trainer: User): Button {
        val buttonInvite = Button(this)
        buttonInvite.height = buttonMain.measuredHeight
        buttonInvite.width = (metrics.widthPixels * 0.2).toInt()
        buttonInvite.background = applicationContext.getDrawable(R.drawable.bottom_border_blue)
        buttonInvite.typeface = typeface
        if(isInvitationPending()) {
            buttonInvite.text = "Wysłano"
            buttonInvite.isEnabled = false
        }
        else
            buttonInvite.text = "Zaproś"
        buttonInvite.setOnClickListener {
            pendingInvitation = PendingInvitationDto(dbContext!!.invitationDao().getById(dbContext.invitationDao().insert(Invitation(loggedUser.userId, trainer.id)))!!)
            setContent()
        }
        return buttonInvite
    }

    private fun isInvitationPending() = pendingInvitation!!.id != null

    @SuppressLint("SetTextI18n")
    private fun setDeleteButton(buttonMain: Button, metrics: DisplayMetrics, typeface: Typeface?, dbContext: GymAssistantDatabase): Button {
        val buttonDelete = Button(this)
        buttonDelete.height = buttonMain.measuredHeight
        buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
        buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
        buttonDelete.text = "Usuń"
        buttonDelete.typeface = typeface

        buttonDelete.setOnClickListener {
            if(isTrainerAssignedToUser()) {
                val currentUser = dbContext.userDao().getById(loggedUser.userId!!)
                currentUser.trainerId = null
                dbContext.userDao().update(currentUser)
                loggedUser = UserDto(dbContext.userDao().getById(loggedUser.userId!!))
                Toast.makeText(this, "Trener usunięty!", Toast.LENGTH_LONG).show()
                finish()
//                val intentManageTrainer = Intent(this, ManageTrainerActivity::class.java)
//                intentManageTrainer.putExtra("loggedUser", loggedUser)
//                startActivity(intentManageTrainer)
            } else {
                dbContext.invitationDao().delete(dbContext.invitationDao().getById(pendingInvitation!!.id!!)!!)
                pendingInvitation = PendingInvitationDto()
                setContent()
            }
        }
        return buttonDelete
    }

    private fun setBackButton() {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
//            if(isTrainerAssignedToUser()) {
//                val intentManageTrainer = Intent(this, MainMenuActivity::class.java)
//                intentManageTrainer.putExtra("loggedUser", loggedUser)
//                startActivity(intentManageTrainer)
//            } else {
//                val intentManageTrainer = Intent(this, ManageTrainerActivity::class.java)
//                intentManageTrainer.putExtra("loggedUser", loggedUser)
//                startActivity(intentManageTrainer)
//            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}