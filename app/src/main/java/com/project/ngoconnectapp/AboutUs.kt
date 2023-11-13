package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.ngoconnectapp.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val database = Firebase.database
//        val feedbackRef = database.getReference("feedback");
         val auth  = FirebaseAuth.getInstance()



      binding.btnSendFeedback.setOnClickListener{
//          val userFeedback = binding.feedback.text.toString();
//          if (!TextUtils.isEmpty(userFeedback)) {
//              val feedbackKey = feedbackRef.push().key;
//              val timestamp = System.currentTimeMillis();
//              val feedback = Feedback(timestamp, userFeedback);
//              feedbackKey?.let { key ->
//                  feedbackRef.child(key).setValue(feedback)
//                  Toast.makeText(this@AboutUs, "Feedback submitted!", Toast.LENGTH_SHORT).show();
//              }
//
//              binding.feedback.text.clear();
//          } else {
//              Toast.makeText(this@AboutUs, "Please enter feedback", Toast.LENGTH_SHORT).show();
//          }
          val userFeedback = binding.feedback.text.toString();
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
          intent.putExtra(Intent.EXTRA_EMAIL, recipients)
          intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
          intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailBody))
          intent.setPackage("com.google.android.gm")
          startActivity(Intent.createChooser(intent, "Send email.."))


      }
      }

    }
