package com.example.projectandroid.game.unscramble

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentUnscrambleIntroBinding
import com.example.projectandroid.game.observation.ObservationDifficulty
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UnscrambleIntroFragment : Fragment() {

    private var _binding: FragmentUnscrambleIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnscrambleIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_unscrambleIntroFragment_to_unscrambleGameFragment)
        }

        // TODO: export to string file
        binding.imgBtnHint.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.observation_difficulty_description_label))
                .setMessage(
                    "Słowa o długości znaków\n\n" +
                            "do 4 -> 5 punktów\n\n" +
                            "5 do 8 -> 10 punktów\n\n" +
                            "9 i więcej -> 15 punktów"
                )
                .show()
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}