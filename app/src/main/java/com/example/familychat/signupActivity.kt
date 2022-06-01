package com.example.familychat

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.familychat.databinding.ActivitySignupBinding
import com.example.familychat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signupActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivitySignupBinding
    lateinit var database:FirebaseDatabase
    lateinit var reference:DatabaseReference
    lateinit var process: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intentToSignin: Intent = Intent(this, signinActivity::class.java)
        binding.haveAccount.setOnClickListener{
            startActivity(intentToSignin)
        }

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        process = ProgressDialog(this)
        process.setTitle("Creating account")
        process.setMessage("We're waiting your account")

        binding.signinSignup.setOnClickListener{
            if(!binding.userNameSignup.getText().isEmpty() &&
                !binding.passwordSignup.getText().isEmpty()&&
                    !binding.editTextTextEmailAddress.getText().isEmpty()){
                process.show()
                mAuth.createUserWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString()
                                                        ,binding.passwordSignup.getText().toString()).
                                        addOnCompleteListener {
                                            process.dismiss()
                                            if(it.isSuccessful){
                                                val user: UserModel =
                                                    UserModel(
                                                        userName = binding.userNameSignup.getText().toString(),
                                                        password = binding.passwordSignup.getText().toString(),
                                                        mail = binding.editTextTextEmailAddress.getText().toString(),
                                                        userId = (it.result.user?.uid ?: 0).toString()
                                                    )
                                                reference = database.getReference("Users")
                                                val id = (it.result.user?.uid ?: 0).toString()
                                                reference.child(id)
                                                reference.child(id).setValue(user)
//                                                val id = (it.result.user?.uid ?: 0).toString()
//                                                database.reference.child("Users").child("666").setValue(user)
//                                                    .child(id).setValue(user)
                                                Toast.makeText(this, "sign up successful", Toast.LENGTH_SHORT).show()
                                            }
                                            else{
                                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                            }
                                        }

            }
             else{
                 Toast.makeText(this, "Enter Credentials", Toast.LENGTH_SHORT).show()
            }
        }


    }
}