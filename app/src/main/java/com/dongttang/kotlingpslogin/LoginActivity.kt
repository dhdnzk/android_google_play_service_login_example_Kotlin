package com.dongttang.kotlingpslogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()

        googleSignInButton?.setSize(SignInButton.SIZE_WIDE)
        googleSignInButton?.setColorScheme(SignInButton.COLOR_LIGHT)

        googleSignInButton!!.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, GOOGLE_SIGN_IN_CODE)
        }

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GOOGLE_SIGN_IN_CODE -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleGoogleSignInResult(result)
            }

            else -> {
            }
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
        if (result.status.isSuccess) {
            goMainScreen()
        } else {
            Log.d("LOGIN_FAILED_LOG", result.status.statusCode.toString())
        }
    }

    private fun goMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    companion object {

        val GOOGLE_SIGN_IN_CODE = 1001
    }

}

