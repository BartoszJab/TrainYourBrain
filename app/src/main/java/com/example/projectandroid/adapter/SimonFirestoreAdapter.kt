package com.example.projectandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectandroid.databinding.OneValueGameListItemBinding
import com.example.projectandroid.models.Simon
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SimonFirestoreAdapter(options: FirestoreRecyclerOptions<Simon>) :
    FirestoreRecyclerAdapter<Simon, SimonFirestoreAdapter.SimonViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimonViewHolder {
        val itemBinding =
            OneValueGameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimonViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SimonViewHolder, position: Int, model: Simon) {
        holder.binding.tvScore.text = model.highestRound.toString()
        holder.binding.tvUsername.text = model.user.username
        holder.binding.tvScoreLabel.text = "Highest round:"
    }

    inner class SimonViewHolder(val binding: OneValueGameListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}