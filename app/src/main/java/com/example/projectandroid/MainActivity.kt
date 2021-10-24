package com.example.projectandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectandroid.auth.AuthActivity
import com.example.projectandroid.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAuth = FirebaseAuth.getInstance()
        finish()
        if (myAuth.currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }
}