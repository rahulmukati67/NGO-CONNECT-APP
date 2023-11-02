package com.project.ngoconnectapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.ngoconnectapp.databinding.ActivityNgodetailPageBinding

class NGODetailPage : AppCompatActivity() {
    private lateinit var binding:ActivityNgodetailPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNgodetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val type = intent.getStringExtra("type")
        val phoneNo = intent.getStringExtra("phoneNo")
        val email = intent.getStringExtra("email")
        val website = intent.getStringExtra("website")

        binding.ngoName.text = name
        binding.ngoType.text = type
        binding.ngoPhoneNo.text = phoneNo
        binding.ngoEmail.text = email
        binding.ngoWeb.text = website

        binding.ngoWeb.setOnClickListener {
            var link = binding.ngoWeb.text.toString()
            if(!link.contains("https://")){
                link = "https://$link"
            }
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        binding.ngoEmail.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(binding.ngoEmail.text.toString())
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send email"))

        }

        binding.ngoPhoneNo.setOnClickListener{
            val number = Uri.parse("tel:" + binding.ngoPhoneNo.text.toString())
            val intent = Intent(Intent.ACTION_DIAL, number)
            startActivity(intent)

        }

    }
}