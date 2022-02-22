package com.example.kidstrackingparent.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kidstrackingparent.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intentTitle = intent.getStringExtra("name")
        val intentImage = intent.getStringExtra("img")

        Log.d("DetailActivity", "onCreate: " + intentTitle)
        Log.d("DetailActivity", "onCreate: " + intentImage)
    }
}