package com.project.ngoconnectapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.ngoconnectapp.databinding.ActivityNgodetailPageBinding
import com.squareup.picasso.Picasso

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
        val imageUri = intent.getStringExtra("image")

        binding.ngoNameDetail.text = name
        binding.ngoTypeDetail.text = type
        binding.ngoPhoneNoDetail.text = phoneNo
        binding.ngoEmailDetail.text = email
        binding.ngoWebDetail.text = website


        if(imageUri != null){
            Picasso.get().load(imageUri.toString()).into(binding.ngoImage)
        }

        binding.ngoWebDetail.setOnClickListener {
            var link = binding.ngoWebDetail.text.toString()
            if(!link.contains("https://")){
                link = "https://$link"
            }
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        binding.ngoEmailDetail.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(binding.ngoEmailDetail.text.toString())
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send email"))

        }

        binding.ngoPhoneNoDetail.setOnClickListener{
            val number = Uri.parse("tel:" + binding.ngoPhoneNoDetail.text.toString())
            val intent = Intent(Intent.ACTION_DIAL, number)
            startActivity(intent)

        }

    }
}