package com.example.projectandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectandroid.databinding.OneValueGameListItemBinding
import com.example.projectandroid.models.MathGame
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MathFirestoreAdapter(options: FirestoreRecyclerOptions<MathGame>) :
    FirestoreRecyclerAdapter<MathGame, MathFirestoreAdapter.MathViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MathViewHolder {
        val itemBinding = OneValueGameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MathViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MathViewHolder, position: Int, model: MathGame) {
        holder.binding.tvScore.text = model.highestScore.toString()
        holder.binding.tvUsername.text = model.user.username
        holder.binding.tvScoreLabel.text = "Highest score:"
    }

    inner class MathViewHolder(val binding: OneValueGameListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}