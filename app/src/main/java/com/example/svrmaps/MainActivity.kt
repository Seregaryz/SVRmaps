package com.example.svrmaps

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.svrmaps.databinding.ActivityMainBinding
import com.example.svrmaps.ui.main.MainFragment
import com.example.svrmaps.ui.sign_up.SignUpFragment
import com.example.svrmaps.utils.newRootScreen
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this.applicationContext)
        supportFragmentManager.newRootScreen(MainFragment::class.java)
    }
}