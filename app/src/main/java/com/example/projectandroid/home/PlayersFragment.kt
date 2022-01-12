package com.example.projectandroid.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectandroid.adapter.MathFirestoreAdapter
import com.example.projectandroid.adapter.ObservationFirestoreAdapter
import com.example.projectandroid.adapter.SimonFirestoreAdapter
import com.example.projectandroid.adapter.UnscrambleFirestoreAdapter
import com.example.projectandroid.databinding.FragmentPlayersBinding
import com.example.projectandroid.models.MathGame
import com.example.projectandroid.models.Observation
import com.example.projectandroid.models.Simon
import com.example.projectandroid.models.Unscramble
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query


class PlayersFragment : Fragment() {

    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvScoreboard.layoutManager = LinearLayoutManager(requireContext())


        setProperAdapter(binding.tabLayout.selectedTabPosition)

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setProperAdapter(tab?.position!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun setProperAdapter(tabPosition: Int) {
        when (tabPosition) {
            0 -> binding.rvScoreboard.adapter = SimonFirestoreAdapter(getSimonGameRecyclerOptions())
            1 -> binding.rvScoreboard.adapter = UnscrambleFirestoreAdapter(getUnscrambleGameRecyclerOptions())
            2 -> binding.rvScoreboard.adapter = ObservationFirestoreAdapter(getObservationGameRecyclerOptions())
            3 -> binding.rvScoreboard.adapter = MathFirestoreAdapter(getMathGameRecyclerOptions())
        }
    }

    private fun getSimonGameRecyclerOptions(): FirestoreRecyclerOptions<Simon> {
        val query = FirebaseFirestore.getInstance()
            .collection("simon")
            .whereGreaterThan("highestRound", 1)
            .orderBy("highestRound", Query.Direction.DESCENDING)
            .limit(20)

        return FirestoreRecyclerOptions.Builder<Simon>()
            .setQuery(query, Simon::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
    }

    private fun getUnscrambleGameRecyclerOptions(): FirestoreRecyclerOptions<Unscramble> {
        val query = FirebaseFirestore.getInstance()
            .collection("unscramble")
            .whereGreaterThan("highestScore", 0)
            .orderBy("highestScore", Query.Direction.DESCENDING)
            .limit(20)

        return FirestoreRecyclerOptions.Builder<Unscramble>()
            .setQuery(query, Unscramble::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
    }

    // TODO: add order by
    private fun getObservationGameRecyclerOptions(): FirestoreRecyclerOptions<Observation> {
        val query = FirebaseFirestore.getInstance()
            .collection("observation")
            .whereGreaterThan("points", 0)
            .orderBy("points", Query.Direction.DESCENDING)
            .limit(20)

        return FirestoreRecyclerOptions.Builder<Observation>()
            .setQuery(query, Observation::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
    }

    private fun getMathGameRecyclerOptions(): FirestoreRecyclerOptions<MathGame> {
        val query = FirebaseFirestore.getInstance()
            .collection("math")
            .whereGreaterThan("highestScore", 0)
            .orderBy("highestScore", Query.Direction.DESCENDING)
            .limit(20)

        return FirestoreRecyclerOptions.Builder<MathGame>()
            .setQuery(query, MathGame::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}