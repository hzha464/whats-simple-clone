package com.example.familychat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familychat.adapter.chatAdapter
import com.example.familychat.adapter.chatDetailAdapter
import com.example.familychat.databinding.ActivityChatDetailBinding
import com.example.familychat.model.MessageModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatDetail : AppCompatActivity() {
    lateinit var binding: ActivityChatDetailBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val senderId = mAuth.uid
        var receiverId: String? = intent.getStringExtra("userId")
        var receiverPic = intent.getStringExtra("proficPic")
        var userNmae = intent.getStringExtra("userName")

        binding.userName.setText(userNmae)
        if(receiverPic != "") {
            Picasso.get().load(receiverPic).placeholder(R.drawable.default_head_image).into(binding.chatDetailHead)

        }

        binding.chatDetailArrow.setOnClickListener{
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        var chatMessages: MutableList<MessageModel> = ArrayList()
        var chatDetailAdapter: chatDetailAdapter = chatDetailAdapter(chatMessages, this, receiverId.toString())
        binding.chatDetailRecycle.adapter = chatDetailAdapter
        binding.chatDetailRecycle.layoutManager= LinearLayoutManager(this)

        var senderRoom: String = senderId + receiverId
        var receiverRoom: String = receiverId + senderId
        database.getReference().child("chats")
            .child(senderRoom)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatMessages.clear()
                    for(dataSnapShot: DataSnapshot in snapshot.children){
                        var listMessage: MessageModel? = dataSnapShot.getValue(MessageModel::class.java)
                        listMessage?.messageId = dataSnapShot.key.toString()
                        if (listMessage != null) {
                            chatMessages.add(listMessage)
                        }
                    }
//                    var newMessage:MessageModel? = snapshot.children.last().getValue(MessageModel::class.java)
//                    if (newMessage != null) {
//                        chatMessages.add(newMessage)
//                    }
                    chatDetailAdapter.notifyDataSetChanged()
//                    chatDetailAdapter.notifyItemInserted(chatMessages.size)
//                    var listMessage: MessageModel? = snapshot.children.last().getValue(MessageModel::class.java)
//                    if(listMessage!= null){
//                        chatMessages.add(listMessage)
//                    }
//                    chatDetailAdapter.notifyItemInserted(snapshot.children.count())
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        binding.chatDetailSend.setOnClickListener {
            var single_message = binding.chatDetailMessage.text.toString()
            if(senderId != null) {
                var OneMessage: MessageModel = MessageModel(senderId, single_message)
                OneMessage.timeStamp = Date().time
                binding.chatDetailMessage.setText("")
                database.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(OneMessage).addOnCompleteListener {
                        database.getReference().child("chats")
                            .child(receiverRoom)
                            .push()
                            .setValue(OneMessage)
                    }

            }

        }


    }
}