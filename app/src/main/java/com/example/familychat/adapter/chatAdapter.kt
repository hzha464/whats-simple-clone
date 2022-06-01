package com.example.familychat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familychat.ChatDetail
import com.example.familychat.R
import com.example.familychat.model.UserModel
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext


class chatAdapter(var userList: List<UserModel>, var context: Context?): RecyclerView.Adapter<chatAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.profile_image)
        var userName: TextView = itemView.findViewById(R.id.chat_name)
        var lastmessage: TextView = itemView.findViewById(R.id.chat_lastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.single_user, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user: UserModel = userList[position]
        if(user.profilepic != "") {
            Picasso.get().load(user.profilepic).placeholder(R.drawable.default_head_image)
                .into(holder.image)
        }
        holder.userName.setText(user.userName)

        holder.itemView.setOnClickListener{
            var intent: Intent = Intent(context, ChatDetail::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("proficPic", user.profilepic)
            intent.putExtra("userName", user.userName)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}