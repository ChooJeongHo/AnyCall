package com.example.anycall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.anycall.databinding.ActivityMainBinding

private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}