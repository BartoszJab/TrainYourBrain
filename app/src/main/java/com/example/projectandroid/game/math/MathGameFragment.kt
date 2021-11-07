package com.example.projectandroid.game.math

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentMathGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MathGameFragment : Fragment() {

    private var _binding: FragmentMathGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MathGameViewModel by viewModels()
    private lateinit var myCounter: CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMathGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val btnAnswer = buttonsAnswer.random()
            btnAnswer.text = newAnswer.toString()
            btnAnswer.setOnClickListener {
                viewModel.correctGuess()
                restartCounter()
            }

            // set incorrect answer button
            buttonsAnswer.filter { it != btnAnswer }.map { otherButton ->
                otherButton.text =
                    (newAnswer - 10..newAnswer + 10).filter { it != newAnswer }.random().toString()
                otherButton.setOnClickListener {
                    showFinalDialog()
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


}