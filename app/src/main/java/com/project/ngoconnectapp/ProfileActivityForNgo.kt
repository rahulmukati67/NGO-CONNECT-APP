package com.project.ngoconnectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ngoconnectapp.databinding.ActivityProfileForNgoBinding

class ProfileActivityForNgo : AppCompatActivity() {

    private lateinit var binding: ActivityProfileForNgoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileForNgoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}