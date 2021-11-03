package com.example.projectandroid.game.unscramble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.projectandroid.R
import com.example.projectandroid.databinding.ActivityGameBinding
import com.example.projectandroid.databinding.ActivityUnscrambleGameBinding
import com.example.projectandroid.models.Simon
import com.example.projectandroid.models.Unscramble
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UnscrambleGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnscrambleGameBinding
    private val viewModel: UnscrambleViewModel by viewModels()
    private lateinit var myAuth: FirebaseAuth
    private lateinit var unscrambleCollectionRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnscrambleGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAuth = FirebaseAuth.getInstance()
        unscrambleCollectionRef = Firebase.firestore.collection("unscramble")

        binding.skip.setOnClickListener { onSkipWord() }

        binding.btnSubmit.setOnClickListener { onSubmitWord() }

        viewModel.currentScrambledWord.observe(this, { newScrambledWord ->
            binding.tvUnscrambledWord.text = newScrambledWord
        })

        viewModel.score.observe(this, { newScore ->
            binding.score.text = getString(R.string.score, newScore)
        })


        viewModel.currentWordCount.observe(this, { newWordCount ->
            binding.wordCount.text = getString(R.string.word_count, newWordCount, 10)
        })
    }

    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            saveToDatabase()
            showFinalScoreDialog()
        }
    }

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                saveToDatabase()
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    private fun saveToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = unscrambleCollectionRef.document(myAuth.currentUser!!.uid).get().await()
                val highestScore = documentSnapshot.toObject<Unscramble>()?.highestScore ?: 0

                val currentScore = viewModel.score.value!!

                if (currentScore > highestScore) {
                    unscrambleCollectionRef.document(myAuth.currentUser!!.uid)
                        .set(Unscramble(currentScore))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UnscrambleGameActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) {_, _ -> exitGame()}
            .setPositiveButton(getString(R.string.play_again)) {_, _ -> restartGame()}
            .show()
    }

    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    private fun exitGame() {
        this.finish()
    }
}