package com.example.health

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.health.ImageSliderAdapter
import androidx.viewpager2.widget.ViewPager2






class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImageSliderAdapter
    private val images = listOf(R.drawable.slide1, R.drawable.slide2, R.drawable.slide3)
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        adapter = ImageSliderAdapter(this, images)
        viewPager.adapter = adapter

        autoSlideImages()

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun autoSlideImages() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % images.size
                viewPager.currentItem = currentPage
                handler.postDelayed(this, 2000)
            }
        }, 2000)
    }
}
