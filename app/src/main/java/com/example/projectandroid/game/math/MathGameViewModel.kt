package com.example.projectandroid.game.math

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MathGameViewModel : ViewModel() {

    private val _answer = MutableLiveData(0)
    val answer: LiveData<Int>
        get() = _answer

    private val _operation = MutableLiveData<MathOperation>()
    val operation: LiveData<MathOperation>
        get() = _operation

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    companion object {
        const val SCORE_INCREASE = 5
    }

    init {
        getOperation()
    }

    private fun getOperation() {
        val minFromNumber = 2
        val maxUntilNumber = 11

        // not repeating same numbers and operations
        val x = Random.nextInt(from = minFromNumber, until = maxUntilNumber)
        val y = (minFromNumber until maxUntilNumber).filter { it != x }.random()
        val z = (minFromNumber until maxUntilNumber).filter { it != x && it != y}.random()

        val operationXY = MathOperation.Operation.values().random()
        val operationYZ = MathOperation.Operation.values().filter { it != operationXY }.random()


        val operation = MathOperation(x, y, z, operationXY, operationYZ)
        _operation.value = operation
        _answer.value = operation.operationResult
    }

    fun correctGuess() {
        increaseScore()
        getOperation()
    }

    fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun restartGame() {
        _score.value = 0
        getOperation()
    }


}