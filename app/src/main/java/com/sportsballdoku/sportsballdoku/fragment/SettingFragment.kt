package com.sportsballdoku.sportsballdoku.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sportsballdoku.sportsballdoku.R
import com.sportsballdoku.sportsballdoku.databinding.FragmentSettingBinding
import com.sportsballdoku.sportsballdoku.utils.*
import com.sportsballdoku.sportsballdoku.utils.Constants.EN
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_LANGUAGE
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_SOUND
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_VIBRATION
import com.sportsballdoku.sportsballdoku.utils.Constants.RU
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.Reword
import java.util.*


class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding { FragmentSettingBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        soundOnOff(getBoolean(KEY_SOUND))
        vibrationOnOff(getBoolean(KEY_VIBRATION))
        manageLanguage(getString(KEY_LANGUAGE))

        binding.apply {

            btnBackToMenu.setOnClickListener {
                vibrator()
                requireActivity().onBackPressed()
            }

            btnSoundOn.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    if (!getBoolean(KEY_SOUND)) {
                        vibrator()
                        soundOnOff(true)
                        saveBoolean(KEY_SOUND, true)
                        requireActivity().startService(Intent(requireContext(),
                            SoundService::class.java))
                    }
                } else {
                    if (!getBoolean(KEY_SOUND)) {
                        saveBoolean(KEY_SOUND, true)
                        soundOnOff(true)
                        requireActivity().startService(Intent(requireContext(),
                            SoundService::class.java))
                    }
                }
            }

            btnSoundOff.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    soundOnOff(false)
                    saveBoolean(KEY_SOUND, false)
                    requireActivity().stopService(Intent(requireContext(),
                        SoundService::class.java))
                } else {
                    saveBoolean(KEY_SOUND, false)
                    soundOnOff(false)
                    requireActivity().stopService(Intent(requireContext(),
                        SoundService::class.java))
                }
            }

            btnVibrationOn.setOnClickListener {
                saveBoolean(KEY_VIBRATION, true)
                vibrationOnOff(true)
            }

            btnVibrationOff.setOnClickListener {
                saveBoolean(KEY_VIBRATION, false)
                vibrationOnOff(false)
            }

            llEn.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    saveString(KEY_LANGUAGE, EN)
                    manageLanguage(EN)
                    AppLocale.desiredLocale = Locale.ENGLISH
                    Reword.reword(binding.root)
                } else {
                    saveString(KEY_LANGUAGE, EN)
                    manageLanguage(EN)
                    AppLocale.desiredLocale = Locale.ENGLISH
                    Reword.reword(binding.root)

                }

            }

            llRu.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    saveString(KEY_LANGUAGE, RU)
                    manageLanguage(RU)
                    AppLocale.desiredLocale = Locale(RU)
                    Reword.reword(binding.root)

                } else {
                    saveString(KEY_LANGUAGE, RU)
                    manageLanguage(RU)
                    AppLocale.desiredLocale = Locale(RU)
                    Reword.reword(binding.root)

                }
            }


        }
    }


    private fun soundOnOff(isOn: Boolean) {
        if (isOn) {
            binding.apply {
                btnSoundOn.setImageResource(R.drawable.ic_sound_on_selected)
                llSoundOn.setBackgroundResource(R.drawable.bg_sound_on)

                btnSoundOff.setImageResource(R.drawable.ic_sound_off_not_selected)
                llSoundOff.setBackgroundResource(R.drawable.bg_sound_off)
            }

        } else {
            binding.apply {
                btnSoundOn.setImageResource(R.drawable.ic_sound_on_not_selected)
                llSoundOn.setBackgroundResource(R.drawable.bg_sound_off)

                btnSoundOff.setImageResource(R.drawable.ic_sound_off_selected)
                llSoundOff.setBackgroundResource(R.drawable.bg_sound_on)
            }

        }


    }

    private fun vibrationOnOff(isOn: Boolean) {
        if (isOn) {
            binding.apply {
                btnVibrationOn.setImageResource(R.drawable.ic_vibration_on_selected)
                llVibrationOn.setBackgroundResource(R.drawable.bg_sound_on)

                btnVibrationOff.setImageResource(R.drawable.ic_vibration_off_not_selected)
                llVibrationOff.setBackgroundResource(R.drawable.bg_sound_off)
            }

        } else {
            binding.apply {
                btnVibrationOn.setImageResource(R.drawable.ic_vibration_on_not_selected)
                llVibrationOn.setBackgroundResource(R.drawable.bg_sound_off)

                btnVibrationOff.setImageResource(R.drawable.ic_vibration_off_selected)
                llVibrationOff.setBackgroundResource(R.drawable.bg_sound_on)
            }

        }


    }

    private fun manageLanguage(language: String) {
        if (language == EN) {
            binding.apply {
                llEn.setBackgroundResource(R.drawable.bg_sound_on)
                tvEn.setTextColor(resources.getColor(R.color.white))

                llRu.setBackgroundResource(R.drawable.bg_sound_off)
                tvRu.setTextColor(resources.getColor(R.color.blue_1))
            }
        } else if (language == RU) {
            binding.apply {
                llEn.setBackgroundResource(R.drawable.bg_sound_off)
                tvEn.setTextColor(resources.getColor(R.color.blue_1))

                llRu.setBackgroundResource(R.drawable.bg_sound_on)
                tvRu.setTextColor(resources.getColor(R.color.white))
            }

        }
    }


}



