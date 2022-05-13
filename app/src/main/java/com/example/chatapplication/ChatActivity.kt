package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var msgEditText:EditText
    private lateinit var msgImage: ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    var senderRoom: String?=null
    var receiverRoom: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name=intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")
        var senderUid=FirebaseAuth.getInstance().currentUser?.uid

        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid

        supportActionBar?.title=name

        chatRecyclerView=findViewById(R.id.chatRecyclerView)
        msgEditText=findViewById(R.id.msgEditText)
        msgImage=findViewById(R.id.msgImage)
        messageList= ArrayList()
        mDbRef=FirebaseDatabase.getInstance().getReference()
        messageAdapter=messageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        msgImage.setOnClickListener{
                val message=msgEditText.text.toString()
                val messageObject=Message(message,senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }
            msgEditText.setText("")

        }
    }
}