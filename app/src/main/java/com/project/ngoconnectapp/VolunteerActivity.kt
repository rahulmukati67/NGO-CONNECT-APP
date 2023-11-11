package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.project.ngoconnectapp.databinding.ActivityVolunteerBinding

class VolunteerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVolunteerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnVolSubmit.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val volName = binding.volName.text.toString()
            val volEmail = binding.volEmail.text.toString()
            val volPhoneNumber= binding.volPhoneNumber.text.toString()
            val volExperience = binding.volExperience.text.toString()
            val volsNGoInfo = binding.volsNGoInfo.text.toString()
            val volJoinDate = binding.volJoinDate.text.toString()
            val recipients = arrayOf(binding.volsNgoEmail.text.toString())

            val emailSubject = "Volunteering Application"
            val emailBody = """
                
            <html>
            <body>
            <p><strong>Dear Sir/Ma'am,</strong></p>
            <p>I hope this email finds you well. I am writing to apply for a volunteer position in your esteemed organization. Below, you'll find my contact information and application details:</p>
            
            <ul>
            <li><strong>Name:</strong> $volName</li>
            <li><strong>Email:</strong> $volEmail</li>
            <li><strong>Phone:</strong> $volPhoneNumber</li>
            <li><strong>Estimated Start Date:</strong> $volJoinDate</li>
            <li><strong>Experiences:</strong> $volExperience</li>
            <li><strong>Source:</strong> $volsNGoInfo</li>
            </ul>

            <p>I'm truly inspired by the remarkable work that your organization carries out in the community, and I am eager to be a part of your mission. I firmly believe that I can contribute meaningfully to your cause.</p>

            <p>I would greatly appreciate the opportunity to discuss my potential role and contributions. Please let me know the next steps in the application process, and I will be ready to take it from there.</p>

            <p>Thank you for considering my application, and I look forward to the possibility of working with your organization.</p>

            <p><em>Sincerely,</em><br>$volName</p>
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