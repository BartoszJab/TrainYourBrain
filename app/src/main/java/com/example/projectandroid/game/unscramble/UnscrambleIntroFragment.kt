package com.example.projectandroid.game.unscramble

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentUnscrambleIntroBinding

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
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}