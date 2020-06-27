package com.home.traker.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.home.traker.R
import com.home.traker.TrakerApp
import com.home.traker.utils.AppPrefences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        Handler().postDelayed(Runnable {

            //startActivity(Intent(this, LoginActivity::class.java))
            finish()
            if (AppPrefences.getLogin(this)!! && AppPrefences.getRememberMe(this)!!) {
                if (AppPrefences.getIsAdmin(this)!!)
                    startActivity(Intent(this, ListActivity::class.java))
                else
                    startActivity(Intent(this, DriverActivityLogScreen::class.java))
                finish()
            } else {
//                AppPrefences.clearAll(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2500)
    }


}