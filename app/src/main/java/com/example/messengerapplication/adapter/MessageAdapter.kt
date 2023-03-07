package com.example.messengerapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapplication.model.Message
import com.example.messengerapplication.R
import com.google.firebase.auth.FirebaseAuth

//providing the view with data in the format it needs to be displayed.
class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_RECEIVE = 1
        private const val ITEM_SENT = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            //inflate receiver message box view
            val view = LayoutInflater.from(context).inflate(R.layout.receive_layout, parent, false)
            ReceiveViewHolder(view)
        } else {
            //sender message box view
            val view  = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
            SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.textView.text = currentMessage.message
            holder.messageTime.text = currentMessage.time


        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.textView.text = currentMessage.message
            holder.messageTime.text = currentMessage.time

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid!! == (currentMessage.senderID)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    //sends data to the user1 message text-box
    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.text_receive_message)!!
        val messageTime = itemView.findViewById<TextView>(R.id.messageTime)!!
    }

    //sends data to the user2 message text-box
        inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.text_sent_message)!!
        val messageTime = itemView.findViewById<TextView>(R.id.messageTime)!!
    }


}