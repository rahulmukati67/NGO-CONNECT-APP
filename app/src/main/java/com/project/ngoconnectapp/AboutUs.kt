package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.project.ngoconnectapp.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSendFeedback.setOnClickListener {
            val userFeedback = binding.feedback.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = "ngoconnectapp@gmail.com"
            val emailSubject = "Feedback For Ngo Connect App"
            val emailBody = """
             
            <html>
            <body>
            <p><strong>Dear Sir/Ma'am,</strong></p>
            <p> FeedBack : $userFeedback </p>
      
            </body>
            </html>
            """


            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipients))
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailBody))
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send email.."))
            binding.feedback.text.clear()

        }
    }

}
