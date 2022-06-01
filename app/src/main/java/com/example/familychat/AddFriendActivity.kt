package com.example.familychat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.familychat.databinding.ActivityAddFriendBinding
import com.example.familychat.databinding.ActivitySigninBinding
import com.example.familychat.model.MessageModel
import com.example.familychat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddFriendActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddFriendBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        setContentView(binding.root)
        binding.AddButtonAddFriend.setOnClickListener {
//            var snapshot: DataSnapshot = database.getReference().child("Users").get().result
//            for (item in snapshot.children) {
//                var listMessage: UserModel? = item.getValue(UserModel::class.java)
//                if (listMessage != null) {
//                    if (binding.FiendEmailAddFriend.text.toString() == listMessage.mail) {
//                        listMessage.friendListEmail.add(binding.FiendEmailAddFriend.text.toString())
//                        database.getReference().child("Users").child(listMessage.userId)
//                            .setValue(listMessage)
//                    }
//                }
//            }
        database.getReference().child("Users").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapShot: DataSnapshot in snapshot.children){
                    var listMessage: UserModel? = dataSnapShot.getValue(UserModel::class.java)
                    if (listMessage != null) {
                        if (binding.FiendEmailAddFriend.text.toString() == listMessage.mail) {
                            if(!listMessage.friendListEmail.contains(binding.FiendEmailAddFriend.text.toString())) {
                                listMessage.friendListEmail.add(binding.FiendEmailAddFriend.text.toString())
                                database.getReference().child("Users").child(listMessage.userId)
                                    .child("Friendmail")
                                    .setValue(listMessage.mail)
                                Toast.makeText(
                                    applicationContext,
                                    "addFriend ${listMessage.mail}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else{
                                Toast.makeText(
                                    applicationContext,
                                    "already exist ${listMessage.mail}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        }
    }
}