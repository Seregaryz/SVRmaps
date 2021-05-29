package com.example.svrmaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.svrmaps.databinding.ActivityMainBinding
import com.example.svrmaps.fragment.map.MapFragment
import com.example.svrmaps.fragment.sign_up.SignUpFragment
import com.example.svrmaps.utils.newRootScreen

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        supportFragmentManager.newRootScreen(SignUpFragment::class.java)
    }
}