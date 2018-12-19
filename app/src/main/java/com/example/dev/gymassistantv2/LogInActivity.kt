package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.User
import com.facebook.*

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*
import org.json.JSONException
import com.facebook.GraphRequest
import com.facebook.AccessToken


class LogInActivity : Activity() {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val readPermissions = Arrays.asList("public_profile")
    private val requestedFbUserData = "first_name, last_name"
    private var fbUserFirstName : String? = null
    private var fbUserLastName : String? = null

    private var gymAssistantDatabase: GymAssistantDatabase? = null
    private var accessToken: AccessToken? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setLoginButton()
        if (isAccessTokenPresent())
            performLoginAutomatically()
    }

    private fun setLoginButton() {

        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(readPermissions)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                setAccessToken(loginResult.accessToken)
                runFbUserDataFetch()
                performLoginAsUser(getRegisteredUser(loginResult.accessToken.userId))
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

    private fun setAccessToken(accessToken: AccessToken) {
        this.accessToken = accessToken
    }

    private fun runFbUserDataFetch() {
        val graphRequest = GraphRequest.newMeRequest(accessToken) { jsonObject, graphResponse ->
            try {
                fbUserFirstName =  jsonObject.getString("first_name")
                fbUserLastName = jsonObject.getString("last_name")
            } catch (e:JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields",
                requestedFbUserData)
        graphRequest.parameters = parameters

        val thread = Thread(Runnable {
            graphRequest.executeAndWait()
        })
        thread.start()
        thread.join()
    }

    private fun getRegisteredUser(facebookId: String): User {
        gymAssistantDatabase = GymAssistantDatabase.getInstance(this)

        var user = gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())
        if (user == null) user = registerNewUser(facebookId)
        return user
    }

    private fun registerNewUser(facebookId: String): User {
        gymAssistantDatabase?.userDao()?.insert(User(facebookId.toLong(), true, fbUserFirstName, fbUserLastName))
        return gymAssistantDatabase?.userDao()?.getByFacebookId(facebookId.toLong())!!
    }

    private fun performLoginAsUser(loggedUser: User) {
        val loggedUserDto = UserDto(loggedUser)
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        intentMainMenu.putExtra("loggedUser", loggedUserDto)
        startActivity(intentMainMenu)
    }

    private fun isAccessTokenPresent(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        if(accessToken != null && !accessToken.isExpired) {
            setAccessToken(accessToken)
            return true
        }
        return false
    }

    private fun performLoginAutomatically() {
        LoginManager.getInstance().logInWithReadPermissions(this, readPermissions)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}