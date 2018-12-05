package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import com.example.dev.gymassistantv2.Database.DbWorkerThread
import com.example.dev.gymassistantv2.Database.TestDatabase
import com.example.dev.gymassistantv2.Entities.Test

class MainMenuActivity : Activity() {

    private var mDb: TestDatabase? = null
    private lateinit var fryty: String
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val buttonBeginWorkout = findViewById<Button>(R.id.buttonBeginWorkout)
        val intentBeginWorkout = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginWorkout.setOnClickListener { startActivity(intentBeginWorkout) }

        val buttonProgress = findViewById<Button>(R.id.buttonProgress)
        val intentProgress = Intent(this, ProgressSubmenuActivity::class.java)
        buttonProgress.setOnClickListener { startActivity(intentProgress) }

        val isTrainer = false // TODO: check if the user is a trainer
        val buttonTrainerOrCharges = findViewById<Button>(R.id.buttonTrainerOrCharges)
        if (!isTrainer) {
            buttonTrainerOrCharges.text = resources.getString(R.string.trainer)
            val intentTrainer = Intent(this, ManageTrainerActivity::class.java)
            buttonTrainerOrCharges.setOnClickListener { startActivity(intentTrainer) }
        } else {
            buttonTrainerOrCharges.text = resources.getString(R.string.your_charges)
            val intentCharges = Intent(this, ChargesSubmenuActivity::class.java)
            buttonTrainerOrCharges.setOnClickListener { startActivity(intentCharges) }
        }

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val intentHistory = Intent(this, WorkoutHistoryActivity::class.java)
        buttonHistory.setOnClickListener { startActivity(intentHistory) }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        val intentSettings = Intent(this, SettingsActivity::class.java)
        buttonSettings.setOnClickListener { startActivity(intentSettings) }

        /*******************************************************************************/

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = TestDatabase.getInstance(this)
        val test = Test()
        test.frytka = "smacznaFrytaXoxoxoxo"
        mDb?.testDao()?.insert(test)

        fetchWeatherDataFromDb()

        print(mDb?.testDao()?.getAll()?.lastOrNull()?.frytka)
    }

    private fun fetchWeatherDataFromDb() {
        val task = Runnable {
            val test =
                    mDb?.testDao()?.getAll()
            mUiHandler.post {
                if (test == null || test?.size == 0) {
                    Toast.makeText(this, "No data in cache..!!", Toast.LENGTH_SHORT)
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }

    private fun insertWeatherDataInDb(weatherData: Test) {
        val task = Runnable { mDb?.testDao()?.insert(weatherData) }
        mDbWorkerThread.postTask(task)
    }

    override fun onDestroy() {
        TestDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
