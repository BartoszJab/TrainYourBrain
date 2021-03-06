package com.example.projectandroid.game.observation

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentObservationGameBinding
import com.example.projectandroid.models.Observation
import com.example.projectandroid.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ObservationGameFragment : Fragment() {

    private var _binding: FragmentObservationGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ObservationGameViewModel by activityViewModels()
    private lateinit var vectorImages: List<Int>

    private lateinit var myAuth: FirebaseAuth
    private lateinit var observationCollectionRef: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObservationGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myAuth = FirebaseAuth.getInstance()
        observationCollectionRef = Firebase.firestore.collection("observation")

        vectorImages = listOf(
            R.drawable.ic_elder_woman, R.drawable.ic_happy, R.drawable.ic_hiking,
            R.drawable.ic_home, R.drawable.ic_pregnant, R.drawable.ic_sad, R.drawable.ic_profile
        )
        viewModel.initializeGame(vectorImages)


        binding.btnSubmit.setOnClickListener {
            if (binding.textInputEditText.text!!.isEmpty()) {
                binding.textInputEditText.error = getString(R.string.guess_cant_be_empty)
            } else {
                val isGuessCorrect =
                    binding.textInputEditText.text.toString() == viewModel.numberOfImageOccurrences.value.toString()
                showFinalDialog(wasGuessCorrect = isGuessCorrect)

                var difficultyString: String
                var points: Int
                when (viewModel.difficulty.value!!) {
                    ObservationDifficulty.EASY -> {
                        difficultyString = "easy"
                        points = ObservationDifficulty.EASY.maxRepetitions
                    }
                    ObservationDifficulty.MEDIUM -> {
                        difficultyString = "medium"
                        points = ObservationDifficulty.MEDIUM.maxRepetitions
                    }
                    ObservationDifficulty.HARD -> {
                        difficultyString = "hard"
                        points = ObservationDifficulty.HARD.maxRepetitions
                    }
                }
                updateObservationDatabase(difficultyString, isGuessCorrect, points.toLong())
            }
        }

        viewModel.imageIdsToShow.observe(viewLifecycleOwner, { newImageIdsToShow ->
            lifecycleScope.launch {
                delay(500)
                for (id in newImageIdsToShow) {
                    binding.ivRandomVector.setImageResource(id)
                    delay(viewModel.difficulty.value!!.imageTimeDuration)
                }

                binding.visibilityChangeGroup.isVisible = true
                binding.ivRandomVector.isVisible = false
                binding.progressBar4.visibility = View.VISIBLE
                delay(1000)
                binding.progressBar4.visibility = View.INVISIBLE
                binding.ivRandomVector.isVisible = true
                binding.ivRandomVector.setImageResource(viewModel.guessImageId.value!!)
            }
        })

    }

    private fun showFinalDialog(wasGuessCorrect: Boolean) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (wasGuessCorrect) getString(R.string.congratulations) else getString(R.string.bad_luck))
            .setMessage(
                if (wasGuessCorrect) getString(R.string.observation_correctly_guessed) else getString(
                    R.string.observation_incorrectly_guessed, viewModel.numberOfImageOccurrences.value
                )
            )
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .show()
    }

    private fun restartGame() {
        binding.ivRandomVector.setImageResource(android.R.color.transparent)
        binding.visibilityChangeGroup.isVisible = false
        binding.textInputEditText.text!!.clear()
        viewModel.initializeGame(vectorImages)
    }

    private fun exitGame() {
        activity?.finish()
    }

    private fun updateObservationDatabase(difficulty: String, wasGuessed: Boolean, points: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = observationCollectionRef.document(myAuth.currentUser!!.uid).get().await()
                val snapshotObservationModel = documentSnapshot.toObject<Observation>()

                if (snapshotObservationModel == null) {
                    val user = User(myAuth.currentUser?.displayName, myAuth.currentUser?.photoUrl.toString())
                    observationCollectionRef.document(myAuth.currentUser!!.uid).set(Observation(user)).await()
                }

                val postfix = if (wasGuessed) "guessed" else "not_guessed"
                observationCollectionRef.document(myAuth.currentUser!!.uid).update("$difficulty.$postfix", FieldValue.increment(1))
                if (postfix == "guessed") observationCollectionRef.document(myAuth.currentUser!!.uid).update("points", FieldValue.increment(points))

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}