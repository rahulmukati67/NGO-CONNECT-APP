package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.project.ngoconnectapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,HomeFragment()).commit()
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()

        binding.navView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_open_24)


        val headerLayout = binding.navView.getHeaderView(0)
        val imgBack = headerLayout.findViewById<ImageButton>(R.id.imgBack)
        val tvUserName = headerLayout.findViewById<TextView>(R.id.tvName)
        if(auth.currentUser != null){
            tvUserName.text = ""
        }
        else{
            tvUserName.text = "Login/Register"
        }
        imgBack.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> {
                if(auth.currentUser != null){
                    val intent = Intent(this,ProfileActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"LOGIN FIRST !",Toast.LENGTH_SHORT).show()
                }

            }
            R.id.nav_myDonations ->{
                if(auth.currentUser != null){
                    val intent2 = Intent(this,MyDonationsActivity::class.java)
                    startActivity(intent2)
                }
                else{
                    val intent2 = Intent(this, RegistrationActivity::class.java)
                    Toast.makeText(this,"To donate , you have to Register first !",Toast.LENGTH_LONG).show()
                    startActivity(intent2)
                }

            }
            R.id.nav_volunteer ->{
                val intent3 = Intent(this, RegistrationActivity::class.java)
                Toast.makeText(this,"To volunteer , you have to Register first !",Toast.LENGTH_LONG).show()
                startActivity(intent3)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}