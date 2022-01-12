package com.example.projectandroid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectandroid.R
import com.example.projectandroid.databinding.ObservationListItemBinding
import com.example.projectandroid.models.Observation
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ObservationFirestoreAdapter(options: FirestoreRecyclerOptions<Observation>) :
    FirestoreRecyclerAdapter<Observation, ObservationFirestoreAdapter.ObservationViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val itemBinding = ObservationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObservationViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int, model: Observation) {
        holder.binding.tvUsername.text = model.user.username

        val easyGuessedPercentage = model.easyGuessedPercentage.toInt()
        val normalGuessedPercentage = model.normalGuessedPercentage.toInt()
        val hardGuessedPercentage = model.hardGuessedPercentage.toInt()

        holder.binding.tvPercentageEasy.text = holder.itemView.context.getString(R.string.observation_game_percentage_value, easyGuessedPercentage)
        holder.binding.tvPercentageNormal.text = holder.itemView.context.getString(R.string.observation_game_percentage_value, normalGuessedPercentage)
        holder.binding.tvPercentageHard.text = holder.itemView.context.getString(R.string.observation_game_percentage_value, hardGuessedPercentage)

        holder.binding.progressCircleEasy.progress = easyGuessedPercentage
        holder.binding.progressCircleNormal.progress = normalGuessedPercentage
        holder.binding.progressCircleHard.progress = hardGuessedPercentage

        Log.d("OBSERVATION", "${model.normalGuessedPercentage.toInt()} ${model.hardGuessedPercentage.toInt()}")
    }

    inner class ObservationViewHolder(val binding: ObservationListItemBinding) : RecyclerView.ViewHolder(binding.root)
}