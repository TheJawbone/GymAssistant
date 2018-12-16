package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import com.example.dev.gymassistantv2.Entities.User

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*

class LogInActivity : Activity() {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var gymAssistantDatabase: GymAssistantDatabase? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        //Call super class constructor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var accessToken : AccessToken? = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn) LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));


        var facebookId : String

        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("public_profile")

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                accessToken = loginResult.accessToken
                facebookId = loginResult.accessToken.userId
                processLogIn(facebookId)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Cancelled!",
                        Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, error.message,
                        Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun processLogIn(facebookId: String) {
        gymAssistantDatabase = GymAssistantDatabase.getInstance(this)

        var registeredUser = gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())
        if (registeredUser == null)
            registeredUser = registerNewUser(facebookId)
        logIn(registeredUser?.isTrainer)
    }

    private fun registerNewUser(facebookId: String): User? {
        gymAssistantDatabase?.userDao()?.insert(User(null, facebookId.toLong()))
        return gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())
    }

    private fun logIn(isTrainer : Boolean?) {
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        intentMainMenu.putExtra("isTrainer", isTrainer)
        startActivity(intentMainMenu)
    }
}