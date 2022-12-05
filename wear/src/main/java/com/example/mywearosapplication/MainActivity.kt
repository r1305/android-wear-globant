package com.example.mywearosapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.mywearosapplication.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            Toast.makeText(this, "Confirmado", Toast.LENGTH_SHORT).show()
            binding.txtTitle.text = "Confirmado"
        }

        binding.btnClose.setOnClickListener {
            Toast.makeText(this, "Denegado", Toast.LENGTH_SHORT).show()
            binding.txtTitle.text = "Denegado"
        }
    }
}