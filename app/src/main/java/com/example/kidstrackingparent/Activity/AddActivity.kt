package com.example.kidstrackingparent.Activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.kidstrackingparent.R
import com.example.kidstrackingparent.dataClass.Childs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        mAuth = FirebaseAuth.getInstance()
        setupPermissions()
        codeScanner()
    }
    private fun codeScanner() {
        codeScanner = CodeScanner(this, scn)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {

                    tv_text.text = it.text
                    getUserData(it.text)

                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun getUserData(uid: String?) {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser


        dbref = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child("Childs")
            .child(uid!!)

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val child = snapshot.getValue(Childs::class.java)
                    var nama = child?.name
                    var photourl = child?.photoUrl

                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                    val UID = Childs(nama, photourl,uid)

                    ref.child("Parents").child(mAuth.currentUser?.uid.toString()).child("Childs").child(uid).setValue(UID).addOnCompleteListener {
                        Log.d("AddActivity", mAuth.currentUser?.uid.toString())

                        val intent = Intent(this@AddActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
//                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}