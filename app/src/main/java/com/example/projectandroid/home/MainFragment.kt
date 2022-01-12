package com.example.projectandroid.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectandroid.databinding.FragmentMainBinding
import com.example.projectandroid.game.math.MathGameActivity
import com.example.projectandroid.game.observation.ObservationGameActivity
import com.example.projectandroid.game.simon.SimonGameActivity
import com.example.projectandroid.game.unscramble.UnscrambleGameActivity


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGame.setOnClickListener {
            startActivity(Intent(activity, SimonGameActivity::class.java))
        }

        binding.btnGame2.setOnClickListener {
            startActivity(Intent(activity, UnscrambleGameActivity::class.java))
        }

        binding.btnGame3.setOnClickListener {
            startActivity(Intent(activity, ObservationGameActivity::class.java))
        }

        binding.btnGame4.setOnClickListener {
            startActivity(Intent(activity, MathGameActivity::class.java))
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}