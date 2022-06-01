package com.example.familychat

import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.familychat.databinding.ActivitySigninBinding
import com.example.familychat.model.UserModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class signinActivity : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    lateinit var process: ProgressDialog
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase

    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signupRequest: BeginSignInRequest
    var new_user: UserModel = UserModel()
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true




    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySigninBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()


        process = ProgressDialog(this)
        process.setTitle("Login")
        process.setMessage("Please wait\n validation in process")
        var intent: Intent = Intent(this, signupActivity::class.java)
        binding.createAnSignin.setOnClickListener {
            startActivity(intent)
        }

        //////////////////////////////////////
        //////////////////////////////////////
        //////////////////////////////////////
        //////////////////////////////////////
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        signupRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        binding.facebookSignin.setOnClickListener{
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0,0,null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                    Toast.makeText(this, "really?", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
//                    var intent:Intent = Intent(this, signupActivity::class.java)
//                    startActivity(intent)
                    Toast.makeText(this, "sign up an account?", Toast.LENGTH_LONG).show()
                    oneTapClient.beginSignIn(signupRequest)
                        .addOnSuccessListener(this) { result ->
//                            val user = mAuth.currentUser
//                            if (user != null) {
//                                new_user.userId = user.uid
//                            }
//                            if (user != null) {
//                                new_user.userName = user.displayName.toString()
//                            }
//                            if (user != null) {
//                                new_user.profilepic = user.photoUrl.toString()
//                            }
//                            if (user != null) {
//                                new_user.mail = user.email.toString()
//                            }
//                            if (user != null) {
//                                database.getReference("Users").child(user.uid.toString()).setValue(new_user)
//                            }
//                            var intentToMainActitity: Intent =
//                                Intent(this, MainActivity::class.java)
//                            startActivity(intentToMainActitity)
                            Toast.makeText(this, "successssss", Toast.LENGTH_LONG).show()
                        try {
                            startIntentSenderForResult(
                                result.pendingIntent.intentSender, REQ_ONE_TAP,
                                null, 0, 0, 0)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                        }
                    }
                        .addOnFailureListener(this) { e ->
                            // No Google Accounts found. Just continue presenting the signed-out UI.
                            Log.d("TAG", e.localizedMessage)
                        }
                    Log.d("TAG", e.localizedMessage)
                }


        }


        //////////////////////////////////////
        //////////////////////////////////////
        //////////////////////////////////////
        //////////////////////////////////////
        //////////////////////////////////////
        binding.signinSignin.setOnClickListener {
            if (!binding.userNameSignin.text.toString()
                    .isEmpty() && !binding.passwordSignin.text.toString().isEmpty()
            ) {
                process.show()
                mAuth.signInWithEmailAndPassword(
                    binding.userNameSignin.text.toString(),
                    binding.passwordSignin.text.toString()
                )
                    .addOnCompleteListener {

                        process.dismiss()
                        if (it.isSuccessful) {
                            var intentToMainActitity: Intent =
                                Intent(this, MainActivity::class.java)
                            startActivity(intentToMainActitity)

                        } else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG)
                        }
                    }
            }
        }
        if (mAuth.currentUser != null) {
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
    }
//////////////////////////////////////////////////
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
        REQ_ONE_TAP -> {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithCredential:success")
                                    val user = mAuth.currentUser
                                    if (user != null) {
                                        new_user.userId = user.uid
                                    }
                                    if (user != null) {
                                        new_user.userName = user.displayName.toString()
                                    }
                                    if (user != null) {
                                        new_user.profilepic = user.photoUrl.toString()
                                    }
                                    if (user != null) {
                                        new_user.mail = user.email.toString()
                                    }
                                    if (user != null) {
                                        database.getReference("Users").child(user.uid.toString()).setValue(new_user)
                                    }
                                    var intentToMainActitity: Intent =
                                        Intent(this, MainActivity::class.java)
                                    startActivity(intentToMainActitity)
//                                    Toast.makeText(this, "successssss", Toast.LENGTH_LONG).show()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                                }
                            }
                    }
                }

            } catch (e: ApiException) {
                Log.d("TAG", "No ID token!")
            }
        }
    }

}
}