package com.sportsballdoku.sportsballdoku.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sportsballdoku.sportsballdoku.R
import com.sportsballdoku.sportsballdoku.databinding.FragmentGameBinding
import com.sportsballdoku.sportsballdoku.utils.Constants.FINISH_TIME
import com.sportsballdoku.sportsballdoku.utils.Constants.KEY_SOUND
import com.sportsballdoku.sportsballdoku.utils.getBoolean
import com.sportsballdoku.sportsballdoku.utils.vibrator
import com.sportsballdoku.sportsballdoku.utils.viewBinding
import java.util.*

class GameFragment : Fragment(R.layout.fragment_game) {

    private var width: Int = 0
    private var height: Int = 0
    private val binding by viewBinding { FragmentGameBinding.bind(it) }
    private lateinit var mediaPlayer: MediaPlayer
    private var timer: CountDownTimer? = null
    private var dialog: Dialog? = null
    private var presentTime = "01:30"
    private var isPlay = false
    var sudokuBalls = Array(4) {
        arrayOfNulls<ImageView>(4)
    }
    lateinit var solvedSudokuPuzzle: Array<IntArray>
    lateinit var unsolvedSudokuPuzzle: Array<IntArray>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels

        setSquareTable()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.setting_up_the_ball)
        setupUI()
        startGame()

        game()

    }

    private fun setSquareTable() {
        binding.apply {
            table.layoutParams.width = width - 100
            table.layoutParams.height = width - 100

            row1.layoutParams.height = (width - 100) / 4
            row2.layoutParams.height = (width - 100) / 4
            row3.layoutParams.height = (width - 100) / 4
            row4.layoutParams.height = (width - 100) / 4

            bgBall11.layoutParams.width = (width - 100) / 4
            bgBall12.layoutParams.width = (width - 100) / 4
            bgBall13.layoutParams.width = (width - 100) / 4
            bgBall14.layoutParams.width = (width - 100) / 4
            bgBall21.layoutParams.width = (width - 100) / 4
            bgBall22.layoutParams.width = (width - 100) / 4
            bgBall23.layoutParams.width = (width - 100) / 4
            bgBall24.layoutParams.width = (width - 100) / 4
            bgBall31.layoutParams.width = (width - 100) / 4
            bgBall32.layoutParams.width = (width - 100) / 4
            bgBall33.layoutParams.width = (width - 100) / 4
            bgBall34.layoutParams.width = (width - 100) / 4
            bgBall41.layoutParams.width = (width - 100) / 4
            bgBall42.layoutParams.width = (width - 100) / 4
            bgBall43.layoutParams.width = (width - 100) / 4
            bgBall44.layoutParams.width = (width - 100) / 4

            bgBall11.layoutParams.height = (width - 100) / 4
            bgBall12.layoutParams.height = (width - 100) / 4
            bgBall13.layoutParams.height = (width - 100) / 4
            bgBall14.layoutParams.height = (width - 100) / 4
            bgBall21.layoutParams.height = (width - 100) / 4
            bgBall22.layoutParams.height = (width - 100) / 4
            bgBall23.layoutParams.height = (width - 100) / 4
            bgBall24.layoutParams.height = (width - 100) / 4
            bgBall31.layoutParams.height = (width - 100) / 4
            bgBall32.layoutParams.height = (width - 100) / 4
            bgBall33.layoutParams.height = (width - 100) / 4
            bgBall34.layoutParams.height = (width - 100) / 4
            bgBall41.layoutParams.height = (width - 100) / 4
            bgBall42.layoutParams.height = (width - 100) / 4
            bgBall43.layoutParams.height = (width - 100) / 4
            bgBall44.layoutParams.height = (width - 100) / 4
        }
    }

    private fun checkBallTag(): Boolean {
        var result = true
        for (i in 0 until sudokuBalls.size) {
            for (j in 0 until sudokuBalls.size) {
                Log.d("@@@", "checkBallTag: ${sudokuBalls[i][j]?.tag}")
                if (sudokuBalls[i][j]?.tag == null) {
                    result = false
                }
            }
        }
        return result
    }

    private fun game() {
        sudokuBalls = arrayOf(
            arrayOf(binding.ball11, binding.ball12, binding.ball13, binding.ball14),
            arrayOf(binding.ball21, binding.ball22, binding.ball23, binding.ball24),
            arrayOf(binding.ball31, binding.ball32, binding.ball33, binding.ball34),
            arrayOf(binding.ball41, binding.ball42, binding.ball43, binding.ball44)
        )


        solvedSudokuPuzzle = generateSudokuPuzzle()!!

        addOnClickImageViewListeners(sudokuBalls)

        unsolvedSudokuPuzzle = generateUnsolvedPuzzle(solvedSudokuPuzzle, 0.75)

        populatePuzzle()


    }

    private fun startGame() {
        isPlay = true
        startTimer(deFormatTime(presentTime))
        binding.btnPlay.setImageResource(R.drawable.ic_pause)
    }

    private fun setupUI() {
        binding.apply {

            btnPlay.setOnClickListener {
                if (!isPlay) {
                    vibrator()
                    isPlay = true
                    btnPlay.setImageResource(R.drawable.ic_pause)
                    startTimer(deFormatTime(binding.tvTimer.text.toString()))
                } else {
                    vibrator()
                    isPlay = false
                    btnPlay.setImageResource(R.drawable.ic_play)
                    stopTimer()
                }
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun startTimer(time: Int) {
        timer = object : CountDownTimer(
            time * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
                binding.tvTimer.text = FINISH_TIME
                showResultDialog(binding.tvTimer.text.toString(), false)
            }
        }

        timer?.start()
    }

    private fun showResultDialog(time: String, gameResult: Boolean) {
        dialog = Dialog(requireContext())
        dialog!!.setContentView(R.layout.result_dialog)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)

        dialog!!.findViewById<TextView>(R.id.tv_result).text = if (gameResult) {
            resources.getString(R.string.you_win)
        } else {
            resources.getString(R.string.you_lose)
        }

        dialog!!.findViewById<TextView>(R.id.tv_time).text = time

        dialog!!.findViewById<TextView>(R.id.btn_back_to_menu).setOnClickListener {
            dialog!!.dismiss()
            vibrator()
            requireActivity().onBackPressed()
        }

        dialog!!.show()
    }

    private fun showChooseBallDialog(imageView: ImageView) {

        dialog = Dialog(requireContext())
        dialog!!.setContentView(R.layout.choose_ball_dialog)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(true)


        dialog!!.apply {
            findViewById<ImageView>(R.id.ball_1).setOnClickListener {
                mediaPlayerStart()
                vibrator()
                imageView.setImageResource(R.drawable.ball_1)
                imageView.tag = 1
                if (checkBallTag()) {
                    if (checkIfSolved(sudokuBalls, solvedSudokuPuzzle)) {
                        showResultDialog(binding.tvTimer.text.toString(), true)
                        stopTimer()
                    }
                }
                this.dismiss()


            }

            findViewById<ImageView>(R.id.ball_2).setOnClickListener {
                mediaPlayerStart()
                vibrator()
                imageView.setImageResource(R.drawable.ball_2)
                imageView.tag = 2
                if (checkBallTag()) {
                    if (checkIfSolved(sudokuBalls, solvedSudokuPuzzle)) {
                        showResultDialog(binding.tvTimer.text.toString(), true)
                        stopTimer()
                    }
                }
                this.dismiss()


            }

            findViewById<ImageView>(R.id.ball_3).setOnClickListener {
                mediaPlayerStart()
                vibrator()
                imageView.setImageResource(R.drawable.ball_3)
                imageView.tag = 3
                if (checkBallTag()) {
                    if (checkIfSolved(sudokuBalls, solvedSudokuPuzzle)) {
                        showResultDialog(binding.tvTimer.text.toString(), true)
                        stopTimer()
                    }
                }
                this.dismiss()
            }

            findViewById<ImageView>(R.id.ball_4).setOnClickListener {
                mediaPlayerStart()
                vibrator()
                imageView.setImageResource(R.drawable.ball_4)
                imageView.tag = 0
                if (checkBallTag()) {
                    if (checkIfSolved(sudokuBalls, solvedSudokuPuzzle)) {
                        showResultDialog(binding.tvTimer.text.toString(), true)
                        stopTimer()
                    }
                }
                this.dismiss()

            }
        }

        dialog!!.show()

    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun deFormatTime(time: String): Int {
        var seconds = 0
        val split = time.split(":")
        if (split[0] == "01") {
            seconds += 60
            seconds += split[1].toInt()
        } else {
            seconds += split[1].toInt()
        }
        return seconds
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun generateSudokuPuzzle(): Array<IntArray>? {
        val solved = Array(4) {
            IntArray(4)
        }

        // Populate the first row of the array
        for (i in 0 until 4) {
            solved[0][i] = i
        }

        // Shuffle the first row
        shuffleArray(solved[0])

        // Populate the remainder of the array
        // k = (k == 9)  ? 0 : k; ... and the two other assignment statements below that
        // handle the array elements being pushed out from the front and to the back.
        var k: Int
        for (i in 1 until 4) {
            for (j in 0 until 4) {
                k = if (i % 2 == 0) {
                    j + 1
                } else {
                    j + 2
                }
                k = if (k == 4) 0 else k
                k = if (k == 5) 1 else k
                k = if (k == 6) 2 else k
                solved[i][j] = solved[i - 1][k]
            }
        }
        return solved
    }

    private fun shuffleArray(array: IntArray) {
        var index: Int
        var temp: Int
        val random = Random()
        for (i in array.size - 1 downTo 1) {
            index = random.nextInt(i + 1)
            temp = array[index]
            array[index] = array[i]
            array[i] = temp
        }
    }

    private fun generateUnsolvedPuzzle(
        solved: Array<IntArray>,
        difficulty: Double,
    ): Array<IntArray> {
        val unsolved = Array(4) {
            IntArray(4)
        }
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (Math.random() > difficulty) {
                    unsolved[i][j] = 0
                } else {
                    unsolved[i][j] = solved[i][j]
                }
            }
        }
        return unsolved
    }

    private fun populatePuzzle() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (unsolvedSudokuPuzzle[i][j] == 0) {

                } else {
                    if (unsolvedSudokuPuzzle[i][j] == 1) {
                        sudokuBalls[i][j]?.apply {
                            setImageResource(R.drawable.ball_1)
                            setBackgroundColor(resources.getColor(R.color.grey_1))
                            isClickable = false
                            tag = 1
                        }

                    } else if (unsolvedSudokuPuzzle[i][j] == 2) {
                        sudokuBalls[i][j]?.apply {
                            setImageResource(R.drawable.ball_2)
                            setBackgroundColor(resources.getColor(R.color.grey_1))
                            isClickable = false
                            tag = 2
                        }


                    } else if (unsolvedSudokuPuzzle[i][j] == 3) {
                        sudokuBalls[i][j]?.apply {
                            setImageResource(R.drawable.ball_3)
                            setBackgroundColor(resources.getColor(R.color.grey_1))
                            isClickable = false
                            tag = 3
                        }


                    } else {
                        sudokuBalls[i][j]?.apply {
                            setImageResource(R.drawable.ball_4)
                            setBackgroundColor(resources.getColor(R.color.grey_1))
                            isClickable = false
                            tag = 4

                        }

                    }

                }
            }
        }
    }

    private fun checkIfSolved(
        sudokuBalls: Array<Array<ImageView?>>,
        solvedSudokuNumbers: Array<IntArray>,
    ): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (sudokuBalls[i][j]?.tag != solvedSudokuNumbers[i][j]) {
                    return false
                }
            }
        }
        return true
    }

    private fun addOnClickImageViewListeners(sudokuBalls: Array<Array<ImageView?>>) {
        for (nums in sudokuBalls) {
            for (num in nums) {
                num?.setOnClickListener {
                    vibrator()
                    showChooseBallDialog(it as ImageView)
                }

            }
        }
    }

    private fun mediaPlayerStart() {
        if (getBoolean(KEY_SOUND)) {
            mediaPlayer.start()
        }
    }

}

