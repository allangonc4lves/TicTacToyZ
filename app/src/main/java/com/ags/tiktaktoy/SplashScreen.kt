package com.ags.tiktaktoy

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.media.tv.TvContract
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logo: ImageView = findViewById(R.id.logo)

        logo.alpha = 0f
        logo.animate().setDuration(5000).alpha(1f).withEndAction{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        }


    }
}