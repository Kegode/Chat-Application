package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpChat : AppCompatActivity() {
    var mTxtName: TextView?=null
    var mTxtEmail: TextView?=null
    var mTxtUserName: TextView?=null
    var mTxtPassword: TextView?=null
    var btnSignUp: Button?=null
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_chat)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        mTxtName=findViewById(R.id.mTxtName)
        mTxtEmail=findViewById(R.id.mTxtEmail)
        mTxtPassword=findViewById(R.id.mTxtPassword)
        mTxtUserName=findViewById(R.id.mTxtUserName)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnSignUp!!.setOnClickListener {
            var name= mTxtName!!.text.toString()
            var email= mTxtEmail!!.text.toString()
            var password=mTxtPassword!!.text.toString()

            signUp(name,email,password)
        }
    }
    private fun signUp(name:String,email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent= Intent(this@SignUpChat,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpChat, "An Error occured",Toast.LENGTH_SHORT).show()

                }
            }
    }
    private fun addUserToDatabase(name: String,email: String,uid:String){
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}