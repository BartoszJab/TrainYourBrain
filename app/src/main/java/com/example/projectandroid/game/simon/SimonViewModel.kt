package com.example.projectandroid.game.simon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class SimonViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _roundNumber = MutableLiveData(1)
    val roundNumber: LiveData<Int>
        get() = _roundNumber

    private val _generatedSequence = MutableLiveData<Queue<Int>>()
    val generatedSequence: LiveData<Queue<Int>>
        get() = _generatedSequence

    private val _mistakesNumber = MutableLiveData(0)
    val mistakesNumber: LiveData<Int>
        get() = _mistakesNumber

    init {
        generateSequence()
    }

    private fun increaseRoundNumber() {
        _roundNumber.value = _roundNumber.value?.plus(1)
    }

    fun checkUserSequenceCorrectness(buttonIndex: Int): Boolean {
        // check if correct button was pressed
        if (_generatedSequence.value?.element() != buttonIndex) {
            _mistakesNumber.value = _mistakesNumber.value?.plus(1)
            if (_mistakesNumber.value != MAX_MISTAKES) generateSequence()
            return false
        }

        _generatedSequence.value?.remove()

        if (_generatedSequence.value?.size == 0) {
            increaseRoundNumber()
            generateSequence()
        }
        return true
    }

    fun reinitializeGameData() {
        _mistakesNumber.value = 0
        _roundNumber.value = 1
        generateSequence()
    }

    private fun generateSequence() {
        val tempSequence = LinkedList<Int>()
        for (i in 1..(_roundNumber.value!! + 1)) {
            // range 0..8 because we have 9 buttons
            tempSequence.add(Random.nextInt(0..8))
        }
        _generatedSequence.value = tempSequence
    }
}