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
            sharedViewModel.setDifficulty(ObservationGameViewModel.Difficulty.EASY)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }

        binding.btnNormal.setOnClickListener {
            sharedViewModel.setDifficulty(ObservationGameViewModel.Difficulty.NORMAL)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }

        binding.btnHard.setOnClickListener {
            sharedViewModel.setDifficulty(ObservationGameViewModel.Difficulty.HARD)
            findNavController().navigate(R.id.action_observationIntroFragment_to_observationGameFragment)
        }
    }

}