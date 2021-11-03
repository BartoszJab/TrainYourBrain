package com.example.projectandroid.game.unscramble

import android.app.Application
import android.text.Spannable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectandroid.R
import java.util.*

class UnscrambleViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MAX_NUMBER_OF_WORDS = 10
    }

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

    init {
        getNextWord()
    }

    private fun getNextWord() {
        val allWordsList = getApplication<Application>().resources.getStringArray(R.array.words)
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()

        do {
            tempWord.shuffle()
        } while (tempWord.joinToString("").equals(currentWord, false))

        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            // increment _currentWordCount.value
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NUMBER_OF_WORDS) {
            getNextWord()
            return true
        } else false
    }

    private fun increaseScore() {
        // add SCORE_INCREASE to _score.value
        _score.value = _score.value?.plus(getScoreToAdd())
    }

    private fun getScoreToAdd(): Int {
        return when (_currentScrambledWord.value?.length) {
            in 1..4 -> 5
            in 5..8 -> 10
            else -> {
                15
            }
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }
}