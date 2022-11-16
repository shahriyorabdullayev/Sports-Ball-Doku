package com.sportsballdoku.sportsballdoku

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import androidx.core.content.ContextCompat
import com.sportsballdoku.sportsballdoku.databinding.ActivityMainBinding
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_SOUND
import com.sportsballdoku.sportsballdoku.utils.SoundService
import dev.b3nedikt.app_locale.AppLocale


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_1)

        if (SharedPref(this).getBoolean(KEY_SOUND)) {
            startService(Intent(this, SoundService::class.java))
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, SoundService::class.java))
    }


    private val appCompatDelegate: AppCompatDelegate by lazy {
        ViewPumpAppCompatDelegate(
            baseDelegate = super.getDelegate(),
            baseContext = this,
            wrapContext = AppLocale::wrap
        )
    }

    override fun getDelegate(): AppCompatDelegate {
        return appCompatDelegate
    }


}