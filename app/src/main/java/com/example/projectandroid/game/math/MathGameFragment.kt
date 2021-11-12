package com.example.projectandroid.game.math

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentMathGameBinding
import com.example.projectandroid.models.MathGame
import com.example.projectandroid.models.User
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

class MathGameFragment : Fragment() {

    private var _binding: FragmentMathGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MathGameViewModel by viewModels()
    private lateinit var myCounter: CountDownTimer

    private lateinit var myAuth: FirebaseAuth
    private lateinit var mathCollectionRef: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMathGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myAuth = FirebaseAuth.getInstance()
        mathCollectionRef = Firebase.firestore.collection("math")

        val buttonsAnswer =
            listOf(binding.btnAnswer1, binding.btnAnswer2, binding.btnAnswer3, binding.btnAnswer4)



        myCounter = object : CountDownTimer(11000, 1000) {
            override fun onTick(p0: Long) {
                val integerValueOfTimer = (p0 / 1000).toInt()
                binding.tvCountDown.text = integerValueOfTimer.toString()
                if (integerValueOfTimer % 2 == 0) binding.ivHourglass.setImageResource(R.drawable.ic_hourglass_top)
                else binding.ivHourglass.setImageResource(R.drawable.ic_hourglass_bottom)
            }

            override fun onFinish() {
                showFinalDialog()
            }
        }.start()

        viewModel.operation.observe(viewLifecycleOwner, { newOperation ->
            val xyOperationString =
                newOperation.convertEnumOperationToString(newOperation.operationXY)
            val yzOperationString =
                newOperation.convertEnumOperationToString(newOperation.operationYZ)
            binding.tvQuestion.text = getString(
                R.string.math_operation_string,
                newOperation.x,
                xyOperationString,
                newOperation.y,
                yzOperationString,
                newOperation.z
            )
        })

        viewModel.answer.observe(viewLifecycleOwner, { newAnswer ->
            // set correct answer button
            val btnCorrectAnswer = buttonsAnswer.random()
            btnCorrectAnswer.text = newAnswer.toString()
            btnCorrectAnswer.setOnClickListener {
                viewModel.correctGuess()
                restartCounter()
            }

            // set incorrect answer buttons without duplicate values
            val usedAnswers = mutableListOf(newAnswer)
            buttonsAnswer.filter { it != btnCorrectAnswer }.map { otherButton ->
                val wrongAnswer: Int = (newAnswer - 10..newAnswer + 10).filter { it !in usedAnswers }.random()
                usedAnswers.add(wrongAnswer)
                otherButton.text = wrongAnswer.toString()

                otherButton.setOnClickListener {
                    showFinalDialog()
                    saveToDatabase()
                    myCounter.cancel()
                }
            }
        })

        viewModel.score.observe(viewLifecycleOwner, {newScore ->
            binding.tvScore.text = getString(R.string.score, newScore)
        })
    }

    override fun onDetach() {
        super.onDetach()
        myCounter.cancel()
        _binding = null
    }

    private fun showFinalDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.bad_luck))
            .setMessage(getString(R.string.math_dialog_fail_message, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .show()
    }

    private fun exitGame() {
        activity?.finish()
        myCounter.cancel()
    }

    private fun restartGame() {
        viewModel.restartGame()
        myCounter.start()
    }

    private fun restartCounter() {
        myCounter.cancel()
        myCounter.start()
    }

    private fun saveToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = mathCollectionRef.document(myAuth.currentUser!!.uid).get().await()
                val highestScore: Int
                val snapshotMathModel = documentSnapshot.toObject<MathGame>()

                if (snapshotMathModel == null) {
                    val user = User(myAuth.currentUser?.displayName, myAuth.currentUser?.photoUrl.toString())
                    mathCollectionRef.document(myAuth.currentUser!!.uid).set(MathGame(0, user))
                    highestScore = 0
                } else {
                    highestScore = snapshotMathModel.highestScore
                }

                val currentScore = viewModel.score.value!!
                if (currentScore > highestScore) {
                    mathCollectionRef.document(myAuth.currentUser!!.uid)
                        .update("highestScore", currentScore)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}