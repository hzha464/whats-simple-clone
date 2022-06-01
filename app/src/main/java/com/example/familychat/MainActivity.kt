package com.example.familychat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.familychat.adapter.FragmenAdapter
import com.example.familychat.databinding.ActivityMainBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        setContentView(binding.root)
        binding.viewPager.setAdapter(FragmenAdapter(supportFragmentManager))
        binding.tablayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.chat_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                var intent: Intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.log_out -> {
                mAuth.signOut()
                var intent: Intent = Intent(this, signinActivity::class.java)
                startActivity(intent)
            }
            R.id.group_chat -> Toast.makeText(this,"setting",Toast.LENGTH_LONG).show()
            R.id.addFriend ->{
                var intent: Intent = Intent(this, AddFriendActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}