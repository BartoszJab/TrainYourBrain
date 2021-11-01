package com.example.projectandroid.game.simon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class SimonViewModel : ViewModel() {
    private val _roundNumber = MutableLiveData(1)
    val roundNumber: LiveData<Int>
        get() = _roundNumber

    private val _generatedSequence = MutableLiveData<Queue<Int>>()
    val generatedSequence: LiveData<Queue<Int>>
        get() = _generatedSequence

    init {
         generateSequence()
    }

    private fun increaseRoundNumber() {
        _roundNumber.value = _roundNumber.value?.plus(1)
    }

    fun checkUserSequenceCorrectness(buttonIndex: Int) {
        // check if correct button was pressed
        if (_generatedSequence.value?.element() != buttonIndex) {
            // TODO: add an error to one more
            generateSequence()
        } else {
            _generatedSequence.value!!.remove()

            if (_generatedSequence.value!!.size == 0) {
                increaseRoundNumber()
                generateSequence()
            }
        }
    }

    private fun generateSequence() {
        _generatedSequence.value = LinkedList()
        for (i in 1..(_roundNumber.value!! + 1)) {
            // range 0..8 because we have 9 buttons
            _generatedSequence.value?.add(Random.nextInt(0..8))
        }
        _generatedSequence.notifyObserver()
    }

    // an extension method that triggers the observe method of MutableLiveData
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = value
    }
}