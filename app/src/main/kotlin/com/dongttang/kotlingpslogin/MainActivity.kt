package com.dongttang.kotlingpslogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()

    }

    override fun onStart() {

        super.onStart()

        val optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient)

        if (optionalPendingResult.isDone) {

            val result = optionalPendingResult.get()
            handleSignInResult(result)

        } else {
            optionalPendingResult.setResultCallback { googleSignInResult -> handleSignInResult(googleSignInResult) }

        }

    }

    private fun handleSignInResult(result: GoogleSignInResult) {

        if (result.isSuccess) {

            val account = result.signInAccount!!

            nameTextView!!.text = account.displayName
            emailTextView!!.text = account.email
            idTextView!!.text = account.id
            Glide.with(this).load(account.photoUrl).into(profilePhotoImageView!!)

        } else {
            goLoginScreen()
        }

    }

    fun logOut(view: View) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->
            if (status.isSuccess) {
                goLoginScreen()
            } else {
                Toast.makeText(applicationContext, R.string.not_close_session, Toast.LENGTH_SHORT).show()

            }
        }

    }

    fun revoke(view: View) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback { status ->
            if (status.isSuccess) {
                goLoginScreen()
            } else {

                Toast.makeText(applicationContext, R.string.not_revoke, Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun goLoginScreen() {

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }
}


