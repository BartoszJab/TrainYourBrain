package com.example.projectandroid.game.simon

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projectandroid.R
import com.example.projectandroid.databinding.ActivitySimonGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class SimonGameActivity : AppCompatActivity() {

    lateinit var binding: ActivitySimonGameBinding
    private val viewModel: SimonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimonGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttons = listOf<ImageView>(
            binding.iv1, binding.iv2, binding.iv3,
            binding.iv4, binding.iv5, binding.iv6,
            binding.iv7, binding.iv8, binding.iv9
        )

        // set on click listeners for buttons
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                viewModel.checkUserSequenceCorrectness(i)
            }
        }

        viewModel.roundNumber.observe(this, { newRoundNumber ->
            binding.tvRoundNumber.text = getString(R.string.round_number, newRoundNumber)
        })

        viewModel.generatedSequence.observe(this, { newGeneratedSequence ->
            lifecycleScope.launch {
                // window with given flags make user not interact with UI when he's being shown the sequence
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                for (i in newGeneratedSequence) {
                    delay(1000)
                    buttons[i].setColorFilter(Color.GREEN)
                    delay(1000)
                    buttons[i].setColorFilter(Color.GRAY)
                }
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }
}