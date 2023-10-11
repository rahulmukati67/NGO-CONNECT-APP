package com.project.ngoconnectapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.project.ngoconnectapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.navView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_open_24)

        if(savedInstanceState == null){
            replaceFragment(HomeFragment(),"")
        }

        val headerLayout = binding.navView.getHeaderView(0)
        val imgBack = headerLayout.findViewById<ImageButton>(R.id.imgBack)
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
            R.id.nav_profile -> replaceFragment(ProfileFragment(),item.title.toString())
            R.id.nav_volunteer ->replaceFragment(VolunteerFragment(),item.title.toString())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment, title:String){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
        supportActionBar?.title = title
    }


}