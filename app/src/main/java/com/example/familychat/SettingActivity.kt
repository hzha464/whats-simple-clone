package com.example.familychat

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.familychat.databinding.ActivitySettingBinding
import com.example.familychat.databinding.ActivitySigninBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    lateinit var process: ProgressDialog
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var datastore: FirebaseStorage
    lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        datastore = FirebaseStorage.getInstance()
        binding.backArrowSetting.setOnClickListener {
            var intent1: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }

        binding.plusSetting.setOnClickListener {
            var intent: Intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 25)
        }

        binding.saveSetting.setOnClickListener {
            var storageReference: StorageReference  = datastore.reference.child("profile_pic")
                       .child(FirebaseAuth.getInstance().uid!!)

                    if (uri != null) {
                        storageReference.putFile(uri).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot>{
                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                                storageReference.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri>{
                                    override fun onSuccess(p0: Uri?) {
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().uid!!)
                                    .child("profilepic").setValue(p0.toString())
                                    Toast.makeText(applicationContext, "head", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        })
//                        object : OnSuccessListener<UploadTask.TaskSnapshot> {
//                            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
//
//                            }
//                        }

//                            .addOnSuccessListener { OnSuccessListener<Uri> {
//                                database.getReference().child("Users").child(FirebaseAuth.getInstance().uid!!)
//                                    .child("profilePic").setValue(uri.toString())
//                                Toast.makeText(this, "head", Toast.LENGTH_SHORT).show()
//
//                            } }
                    }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 25){
            if (data != null) {
                if(data.data != null){
                    uri = data.data!!
                    binding.headSetting.setImageURI(uri)
//                   var storageReference: StorageReference  = datastore.reference.child("profile_pic")
//                       .child(FirebaseAuth.getInstance().uid!!)
//
//                    if (uri != null) {
//                        storageReference.putFile(uri)
//                    }
                }

            }
        }
    }
}
