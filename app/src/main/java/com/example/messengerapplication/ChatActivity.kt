package com.example.messengerapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messengerapplication.adapter.MessageAdapter
import com.example.messengerapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter

    private val messageList: ArrayList<Message> = ArrayList()
    private lateinit var mDbaseRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }
    private fun initViews() {
        mDbaseRef = FirebaseDatabase.getInstance().reference
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        supportActionBar?.title = name

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageAdapter = MessageAdapter(this, messageList)

        binding.chatRecycleView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        //send information to the database about messages
        mDbaseRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnap in snapshot.children) {
                        val message = postSnap.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    //recyclerView moves to the latest message each time user sends a new message or open chat
                    binding.chatRecycleView.scrollToPosition(messageAdapter.itemCount - 1);
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        binding.messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) {
                    binding.sentButton.isVisible = true
                    binding.sentButton.setOnClickListener {
                        //added date to the message
                        val messageObj = Message(text, senderUid, messageTime = getCurrentDateTime() )
                        mDbaseRef.child("chats").child(senderRoom!!).child("messages").push()
                            .setValue(messageObj).addOnSuccessListener {
                                mDbaseRef.child("chats").child(receiverRoom!!).child("messages").push()
                                    .setValue(messageObj)
                            }
                        binding.messageBox.setText("")
                    }
                } else {
                    binding.sentButton.isVisible = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    //This is a method to get current time in specified format
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss || dd-MM-yyyy ", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }



}