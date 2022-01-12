package com.example.projectandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectandroid.databinding.OneValueGameListItemBinding
import com.example.projectandroid.models.Unscramble
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UnscrambleFirestoreAdapter(options: FirestoreRecyclerOptions<Unscramble>) :
    FirestoreRecyclerAdapter<Unscramble, UnscrambleFirestoreAdapter.UnscrambleViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnscrambleViewHolder {
        val itemBinding = OneValueGameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnscrambleViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UnscrambleViewHolder, position: Int, model: Unscramble) {
        holder.binding.tvScore.text = model.highestScore.toString()
        holder.binding.tvUsername.text = model.user.username
        holder.binding.tvScoreLabel.text = "Highest score:"
    }

    inner class UnscrambleViewHolder(val binding: OneValueGameListItemBinding) : RecyclerView.ViewHolder(binding.root)

}