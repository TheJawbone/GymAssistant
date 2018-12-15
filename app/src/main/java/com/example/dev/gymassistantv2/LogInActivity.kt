package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

class LogInActivity : Activity() {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun logIn() {
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        startActivity(intentMainMenu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //Call super class constructor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var accessToken: AccessToken
        var facebookId = "userId"

        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("public_profile")

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                accessToken = loginResult.accessToken
                facebookId = accessToken.userId
                logIn()
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

}