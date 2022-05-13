package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginChat : AppCompatActivity() {
    var mTxtEmail: TextView?=null
    var mTxtPassword: TextView?=null
    var btnLogin: Button?=null
    var btnSignUp: Button?=null
   private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_chat)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        mTxtEmail=findViewById(R.id.mTxtEmail)
        mTxtPassword=findViewById(R.id.mTxtPassword)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnSignUp!!.setOnClickListener {
            val intent=Intent(this, SignUpChat::class.java)
            startActivity(intent)
        }

        btnLogin!!.setOnClickListener {
            var email= mTxtEmail!!.text.toString()
            var password = mTxtPassword!!.text.toString()

login(email,password)
        }

    }

private fun login(email:String, password:String){
    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val intent=Intent(this@LoginChat,MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginChat,"User does not exist",Toast.LENGTH_SHORT).show()

            }
        }
}
}