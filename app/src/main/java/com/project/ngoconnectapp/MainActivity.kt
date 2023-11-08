package com.project.ngoconnectapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.ngoconnectapp.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef : FirebaseDatabase
    private lateinit var tvUserName: TextView
    private lateinit var profileImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment())
            .commit()
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        binding.navView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_open_24)


        val headerLayout = binding.navView.getHeaderView(0)
        val imgBack = headerLayout.findViewById<ImageButton>(R.id.imgBack)
        profileImage = headerLayout.findViewById(R.id.imgProfile)
        tvUserName = headerLayout.findViewById(R.id.tvName)

        if (auth.currentUser != null) {

            getData()

        } else {
            tvUserName.text = getString(R.string.click_login)
        }

        imgBack.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        tvUserName.setOnClickListener {
            if (tvUserName.text == getString(R.string.click_login)) {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (auth.currentUser != null) {
            getData()
        } else {
            tvUserName.text = getString(R.string.click_login)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                if (auth.currentUser != null) {
                    if (intent.getStringExtra("type") == "user") {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else if (intent.getStringExtra("type") == "ngo") {
                        startActivity(Intent(this, ProfileActivityForNgo::class.java)
                            .putExtra("regId", intent.getStringExtra("regId")))
                    }

                } else {
                    Toast.makeText(this, "REGISTER / LOGIN FIRST !", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.nav_myDonations -> {
                if (auth.currentUser != null) {
                    val intent = Intent(this, MyDonationsActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    Toast.makeText(
                        this,
                        "To donate , you have to Register/Login  !",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intent)
                }

            }

            R.id.nav_volunteer -> {
                if (auth.currentUser != null) {
                    val intent = Intent(this, VolunteerActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    Toast.makeText(
                        this,
                        "To  join as a volunteer , you have to Register/Login !",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intent)
                }

            }

            R.id.nav_find -> {
                val uri = Uri.parse("geo:0,0?q=ngos near me")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)

            }

            R.id.nav_logout -> {
                val googleSignInClient =
                    GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
                googleSignInClient.signOut()
                auth.signOut()
                Toast.makeText(this, "Successfully Logged Out !", Toast.LENGTH_SHORT).show()
                tvUserName.text = getString(R.string.click_login)
                profileImage.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.download))
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getData() {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${auth.currentUser?.uid!!}.jpg")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profileImage.setImageBitmap(bitmap)

        }
        if (intent.getStringExtra("type") == "user") {
            dbRef.getReference("users").child(auth.currentUser!!.uid)
                .child("username").get().addOnCompleteListener {
                    val username = it.result.value
                    tvUserName.text = username.toString()
                }
        }
        else if(intent.getStringExtra("type") == "ngo"){
            dbRef.getReference("ngoDetails").child(intent.getStringExtra("regId").toString())
                .child("name").get().addOnSuccessListener {
                    val name = it.value
                    tvUserName.text = name.toString()
                }
        }

    }


}