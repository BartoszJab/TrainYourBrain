package com.example.projectandroid.game.unscramble

import android.text.Spannable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UnscrambleViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private fun getNextWord() {
        //currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.joinToString("").equals(currentWord, false)) {
            tempWord.shuffle()
            Log.d("getNextWord", "Inside loop")
        }

        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            // increment _currentWordCount.value
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }
}