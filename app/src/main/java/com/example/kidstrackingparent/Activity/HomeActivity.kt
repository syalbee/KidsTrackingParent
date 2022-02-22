package com.example.kidstrackingparent.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidstrackingparent.R
import com.example.kidstrackingparent.adapter.ChildAdapter
import com.example.kidstrackingparent.dataClass.Childs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Childs>
    private lateinit var mAdapter:ChildAdapter

    private companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userArrayList = ArrayList()
        mAdapter = ChildAdapter(this,userArrayList)

        userRecyclerview = findViewById(R.id.rvChildList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)


        mAuth = FirebaseAuth.getInstance()
        getUserData()

        btnLogout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun getUserData() {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        var Uidi : String = currentUser?.uid.toString()

        dbref = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child("Parents")
            .child(Uidi)
            .child("Childs")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (childSnapshot in snapshot.children){
                        val child = childSnapshot.getValue(Childs::class.java)
                        userArrayList.add(child!!)
                    }
                    userRecyclerview.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}