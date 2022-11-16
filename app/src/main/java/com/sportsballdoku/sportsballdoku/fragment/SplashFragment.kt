package com.sportsballdoku.sportsballdoku.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sportsballdoku.sportsballdoku.R


class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()

    }

    private fun startTimer() {
        object : CountDownTimer(1500, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                findNavController().navigate(R.id.action_splashFragment_to_menuFragment)
            }
        }.start()
    }

}