package com.example.projectandroid.game.observation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentObservationIntroBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ObservationIntroFragment : Fragment() {

    private var _binding: FragmentObservationIntroBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: ObservationGameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObservationIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEasy.setOnClickListener {
            sharedViewModel.setDifficulty(ObservationDifficulty.EASY)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }

        binding.btnNormal.setOnClickListener {
            sharedViewModel.setDifficulty(ObservationDifficulty.MEDIUM)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }

        binding.btnHard.setOnClickListener {
            sharedViewModel.setDifficulty(ObservationDifficulty.HARD)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }

        binding.imgBtnHint.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.observation_difficulty_description_label))
                .setMessage(
                    getString(
                        R.string.observation_difficulty_description,
                        getString(R.string.easy),
                        ObservationDifficulty.EASY.maxRepetitions,
                        ObservationDifficulty.EASY.imageTimeDuration / 1000.0
                    ) + "\n\n" +
                    getString(
                        R.string.observation_difficulty_description,
                        getString(R.string.normal),
                        ObservationDifficulty.MEDIUM.maxRepetitions,
                        ObservationDifficulty.MEDIUM.imageTimeDuration / 1000.0
                    ) + "\n\n" +
                    getString(
                        R.string.observation_difficulty_description,
                        getString(R.string.hard),
                        ObservationDifficulty.HARD.maxRepetitions,
                        ObservationDifficulty.HARD.imageTimeDuration / 1000.0
                    )
                )
                .show()
        }
    }

}