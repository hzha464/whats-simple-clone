package com.example.familychat.fragment

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familychat.R
import com.example.familychat.adapter.chatAdapter
import com.example.familychat.databinding.FragmentChatBinding
import com.example.familychat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var userList: MutableList<UserModel>
    lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()
        userList = ArrayList()
        var adapter: chatAdapter = chatAdapter(userList, context)
        binding.chatRecycleView.adapter = adapter
        var layourmanager: LinearLayoutManager = LinearLayoutManager(context)
        binding.chatRecycleView.setLayoutManager(layourmanager)
        val reference = database.reference.child("Users")
        reference.addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        for(dataSnapShot: DataSnapshot in snapshot.children){
                        var ueserNew = dataSnapShot.getValue(UserModel::class.java)
                        if (ueserNew != null) {
                            ueserNew.userId = dataSnapShot.key.toString()
                            if (!ueserNew.userId.equals(FirebaseAuth.getInstance().uid)) {
                                userList.add(ueserNew)
                            }
                        }


                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
        return binding.root

    }

}