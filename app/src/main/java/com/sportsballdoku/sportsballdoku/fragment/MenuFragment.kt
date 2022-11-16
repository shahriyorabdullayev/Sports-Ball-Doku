package com.sportsballdoku.sportsballdoku.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sportsballdoku.sportsballdoku.R
import com.sportsballdoku.sportsballdoku.databinding.FragmentMenuBinding
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_VIBRATION
import com.sportsballdoku.sportsballdoku.utils.getBoolean
import com.sportsballdoku.sportsballdoku.utils.vibrator
import com.sportsballdoku.sportsballdoku.utils.viewBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding { FragmentMenuBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            if (getBoolean(KEY_VIBRATION)) {
                vibrator()
                findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
            } else {
                findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
            }

        }

        binding.btnSetting.setOnClickListener {
            if (getBoolean(KEY_VIBRATION)) {
                vibrator()
                findNavController().navigate(R.id.action_menuFragment_to_settingFragment)
            } else {
                findNavController().navigate(R.id.action_menuFragment_to_settingFragment)
            }

        }


    }



}