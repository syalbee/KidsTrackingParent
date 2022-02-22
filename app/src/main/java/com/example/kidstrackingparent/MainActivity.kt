package com.example.kidstrackingparent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.kidstrackingparent.Activity.HomeActivity
import com.example.kidstrackingparent.Activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        Handler().postDelayed({
            if(user != null){
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }else{
                val signInIntent = Intent(this, LoginActivity::class.java)
                startActivity(signInIntent)
                finish()
            }
        }, 2000)
    }
}