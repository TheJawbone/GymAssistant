package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dev.gymassistantv2.DTOs.UserDto
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
    private val readPermissions = Arrays.asList("public_profile")

    private var gymAssistantDatabase: GymAssistantDatabase? = null
    private var accessToken: AccessToken? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //Call super class constructor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setLoginButton()
        if (isAccessTokenPresent())
            performLoginAutomatically()
    }

    private fun isAccessTokenPresent(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        if(accessToken != null && !accessToken.isExpired) {
            setAccessToken(accessToken)
            return true
        }
        return false
    }

    private fun setAccessToken(accessToken: AccessToken) {
        this.accessToken = accessToken
    }

    private fun performLoginAutomatically() {
        LoginManager.getInstance().logInWithReadPermissions(this, readPermissions)
    }


    private fun setLoginButton() {
        var facebookId : String

        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(readPermissions)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                accessToken = loginResult.accessToken
                facebookId = loginResult.accessToken.userId
                logIntoApp(facebookId)
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

    private fun logIntoApp(facebookId: String) {
        gymAssistantDatabase = GymAssistantDatabase.getInstance(this)

        var user = gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())
        if (user == null)
            user = registerNewUser(facebookId)
        logIn(user!!)
    }

    private fun registerNewUser(facebookId: String): User? {
        gymAssistantDatabase?.userDao()?.insert(User(facebookId.toLong(), true))
        return gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())
    }

    private fun logIn(loggedUser: User) {
        val loggedUserDto = UserDto(loggedUser)
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        intentMainMenu.putExtra("loggedUser", loggedUserDto)
        startActivity(intentMainMenu)
    }

    override fun onBackPressed() {

    }
}