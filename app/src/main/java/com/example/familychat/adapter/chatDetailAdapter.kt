package com.example.familychat.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familychat.R
import com.example.familychat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class chatDetailAdapter(var message: MutableList<MessageModel>, var context: Context, var receiverId: String)
    : RecyclerView.Adapter<chatDetailAdapter.WholeViewHolder>() {
    var SENDER_TYPE = 1
    var RECEIVER_TYPE = 2
    class WholeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var msg: TextView
        lateinit var time: TextView

        fun bindReceiver(){
             msg = itemView.findViewById(R.id.receiverText)
             time = itemView.findViewById(R.id.receiverTime)

        }
        fun bindSender(){
             msg= itemView.findViewById(R.id.senderText)
             time= itemView.findViewById(R.id.senderTime)
        }
    }
//    class SenderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        var senderMsg: TextView = itemView.findViewById(R.id.senderText)
//        var senderTime: TextView = itemView.findViewById(R.id.senderTime)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeViewHolder {
        return if(viewType == SENDER_TYPE){
            var view: View = LayoutInflater.from(context).inflate(R.layout.sender_bubble,parent, false)
            WholeViewHolder(view)
        } else{
            var view: View = LayoutInflater.from(context).inflate(R.layout.receiver_bubble,parent, false)
            WholeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: WholeViewHolder, position: Int) {
        var singleMessage: MessageModel = message[position]
        var viewType = getItemViewType(position)
        print("viewtype $viewType")
        if( viewType ==SENDER_TYPE){
            holder.bindSender()
            holder.msg.setText(singleMessage.message)

            var date = Date(singleMessage.timeStamp)
            var singledate:SimpleDateFormat = SimpleDateFormat("h:mm a")
            var strDate:String = singledate.format(date)
            holder.time.setText(strDate)
        }
        else{
            holder.bindReceiver()
            holder.msg.setText(singleMessage.message)

            var date = Date(singleMessage.timeStamp)
            var singledate:SimpleDateFormat = SimpleDateFormat("h:mm a")
            var strDate:String = singledate.format(date)
            holder.time.setText(strDate)
        }

        holder.itemView.setOnClickListener{
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton(
                    "yes", DialogInterface.OnClickListener {
                            dialogInterface, i ->
                        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
                        var senderRoom: String = FirebaseAuth.getInstance().uid + receiverId
                        database.getReference().child("chats").child(senderRoom)
                            .child(singleMessage.messageId)
                            .removeValue()
                    })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .show()
        }

    }

    override fun getItemCount(): Int {
        return message.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(message[position].uid == FirebaseAuth.getInstance().uid){
            SENDER_TYPE
        } else{
            RECEIVER_TYPE
        }
    }
}