package com.example.projectandroid.game.observation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentObservationGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ObservationGameFragment : Fragment() {

    private var _binding: FragmentObservationGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ObservationGameViewModel by activityViewModels()
    private lateinit var vectorImages: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObservationGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vectorImages = listOf(
            R.drawable.ic_elder_woman, R.drawable.ic_happy, R.drawable.ic_hiking,
            R.drawable.ic_home, R.drawable.ic_pregnant, R.drawable.ic_sad, R.drawable.ic_profile
        )
        viewModel.initializeGame(vectorImages)


        binding.btnSubmit.setOnClickListener {
            Log.d("OBSERVATION", binding.textInputEditText.text.toString())
            Log.d("OBSERVATION", viewModel.numberOfImageOccurrences.value.toString())
            if (binding.textInputEditText.text!!.isEmpty()) {
                binding.textInputEditText.error = "Guess can't be empty"
            } else {
                val isGuessCorrect =
                    binding.textInputEditText.text.toString() == viewModel.numberOfImageOccurrences.value.toString()
                showFinalDialog(wasGuessCorrect = isGuessCorrect)
            }
        }

        viewModel.imageIdsToShow.observe(viewLifecycleOwner, { newImageIdsToShow ->
            Log.d("OBSERVATION", "newImageIdssToShow ${newImageIdsToShow.size}")
            lifecycleScope.launch {
                delay(500)
                for (id in newImageIdsToShow) {
                    binding.ivRandomVector.setImageResource(id)
                    delay(viewModel.difficulty.value!!.imageTimeDuration)
                }
                binding.visibilityChangeGroup.isVisible = true
                binding.ivRandomVector.setImageResource(viewModel.guessImageId.value!!)
            }
        })

    }

    private fun showFinalDialog(wasGuessCorrect: Boolean) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (wasGuessCorrect) getString(R.string.congratulations) else getString(R.string.bad_luck))
            .setMessage(
                if (wasGuessCorrect) getString(R.string.observation_correctly_guessed) else getString(
                    R.string.observation_incorrectly_guessed
                )
            )
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .show()
    }

    private fun restartGame() {
        binding.visibilityChangeGroup.isVisible = false
        binding.textInputEditText.text!!.clear()
        viewModel.initializeGame(vectorImages)
    }

    private fun exitGame() {
        activity?.finish()
    }

}