package com.example.projectandroid.game.simon

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projectandroid.R
import com.example.projectandroid.databinding.ActivitySimonGameBinding
import com.example.projectandroid.models.Simon
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.concurrent.schedule

class SimonGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimonGameBinding
    private val viewModel: SimonViewModel by viewModels()
    private lateinit var heartsViews: List<ImageView>
    private lateinit var myAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimonGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAuth = FirebaseAuth.getInstance()
        val simonCollectionRef = Firebase.firestore.collection("simon")

        val buttons = listOf(
            binding.iv1, binding.iv2, binding.iv3,
            binding.iv4, binding.iv5, binding.iv6,
            binding.iv7, binding.iv8, binding.iv9
        )

        heartsViews = listOf(
            binding.ivHeart1, binding.ivHeart2
        )

        // set on click listeners for buttons
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                lifecycleScope.launch {
                    if (viewModel.checkUserSequenceCorrectness(i)) {
                        buttons[i].setColorFilter(Color.GREEN)
                    } else {
                        buttons[i].setColorFilter(Color.RED)
                    }
                    delay(300)
                    buttons[i].setColorFilter(Color.GRAY)
                }

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
                    delay(600)
                    buttons[i].setColorFilter(Color.GREEN)
                    delay(600)
                    buttons[i].setColorFilter(Color.GRAY)
                }
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        viewModel.mistakesNumber.observe(this, { newMistakesNumber ->
            if (newMistakesNumber > 0) {
                heartsViews[newMistakesNumber - 1].setImageResource(R.drawable.ic_heart_broken)
                if (newMistakesNumber == MAX_MISTAKES) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val documentSnapshot = simonCollectionRef.document(myAuth.currentUser!!.uid).get().await()
                            val highestRound = documentSnapshot.toObject<Simon>()?.highestRound

                            // if round number is better than previous one then update the value
                            val currentRoundNumber = viewModel.roundNumber.value!!
                            if (currentRoundNumber > highestRound!!) {
                                simonCollectionRef.document(myAuth.currentUser!!.uid)
                                    .set(Simon(currentRoundNumber))
                            }

                            //simonCollectionRef.add(Simon(viewModel.roundNumber.value!!))
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SimonGameActivity, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    showFinalDialog()
                }
            }
        })
    }

    private fun showFinalDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Game Over")
            .setMessage("You've lost at round number ${viewModel.roundNumber.value}")
            .setCancelable(false)
            .setNegativeButton("Exit") {_, _ -> exitGame()}
            .setPositiveButton("Play again") {_, _ -> restartGame()}
            .show()
    }

    private fun exitGame() {
        this.finish()
    }

    private fun restartGame() {
        viewModel.reinitializeGameData()
        for (heart in heartsViews) {
            heart.setImageResource(R.drawable.ic_heart)
        }
    }

}