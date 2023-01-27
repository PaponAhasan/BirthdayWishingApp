package com.example.birthdaywishing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.birthdaywishing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        fetchAndSendDataActivity()

        setContentView(view)
    }

    private fun fetchAndSendDataActivity() {
        binding.happyBirthDayBtn.setOnClickListener {
            val name = binding.EtPerson.editableText.toString()
            if (name.isEmpty()) Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
            else {
                val intent = Intent(this, HappyBirthDayActivity::class.java)
                intent.putExtra(HappyBirthDayActivity.NAME_EXTRA, name)
                startActivity(intent)
            }
        }
    }
}