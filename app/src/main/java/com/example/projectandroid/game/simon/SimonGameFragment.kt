package com.example.projectandroid.game.simon

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentSimonGameBinding
import com.example.projectandroid.models.MathGame
import com.example.projectandroid.models.Simon
import com.example.projectandroid.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SimonGameFragment : Fragment() {

    private var _binding: FragmentSimonGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SimonViewModel by viewModels()
    private lateinit var heartsViews: List<ImageView>
    private lateinit var myAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSimonGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.roundNumber.observe(viewLifecycleOwner, { newRoundNumber ->
            binding.tvRoundNumber.text = getString(R.string.round_number, newRoundNumber)
        })

        viewModel.generatedSequence.observe(viewLifecycleOwner, { newGeneratedSequence ->
            lifecycleScope.launch {

                // window with given flags make user not interact with UI when he's being shown the sequence
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                for (i in newGeneratedSequence) {
                    delay(600)
                    binding.progressBar3.isVisible = true
                    buttons[i].setColorFilter(Color.GREEN)
                    delay(600)
                    buttons[i].setColorFilter(Color.GRAY)
                }
                binding.progressBar3.isVisible = false
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        viewModel.mistakesNumber.observe(viewLifecycleOwner, { newMistakesNumber ->
            if (newMistakesNumber > 0) {
                if (heartsViews.size >= newMistakesNumber) heartsViews[newMistakesNumber - 1].setImageResource(R.drawable.ic_heart_broken)

                if (newMistakesNumber == MAX_MISTAKES) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val documentSnapshot = simonCollectionRef.document(myAuth.currentUser!!.uid).get().await()
                            val highestRound: Int
                            val snapshotSimonModel = documentSnapshot.toObject<Simon>()

                            if (snapshotSimonModel == null) {
                                val user = User(myAuth.currentUser?.displayName, myAuth.currentUser?.photoUrl.toString())
                                simonCollectionRef.document(myAuth.currentUser!!.uid).set(Simon(0, user))
                                highestRound = 0
                            } else {
                                highestRound = snapshotSimonModel.highestRound
                            }

                            // if round number is better than previous one then update the value
                            val currentRoundNumber = viewModel.roundNumber.value!!
                            if (currentRoundNumber > highestRound) {
                                simonCollectionRef.document(myAuth.currentUser!!.uid)
                                    .update("highestRound", currentRoundNumber)
                            }

                            //simonCollectionRef.add(Simon(viewModel.roundNumber.value!!))
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    showFinalDialog()
                }
            }
        })
    }

    private fun showFinalDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.bad_luck))
            .setMessage(getString(R.string.simon_game_summary, viewModel.roundNumber.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) {_, _ -> exitGame()}
            .setPositiveButton(getString(R.string.play_again)) {_, _ -> restartGame()}
            .show()
    }

    private fun exitGame() {
        activity?.finish()
    }

    private fun restartGame() {
        viewModel.reinitializeGameData()
        for (heart in heartsViews) {
            heart.setImageResource(R.drawable.ic_heart)
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}