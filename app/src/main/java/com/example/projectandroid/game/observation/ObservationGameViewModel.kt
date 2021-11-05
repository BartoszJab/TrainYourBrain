package com.example.projectandroid.game.observation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ObservationGameViewModel : ViewModel() {

    companion object {
        var MAX_REPETITIONS: Int = 0
    }

    enum class Difficulty {
        EASY, NORMAL, HARD
    }

    private val _imageIdsToShow = MutableLiveData(mutableListOf<Int>())
    val imageIdsToShow: LiveData<MutableList<Int>>
        get() = _imageIdsToShow

    private val _guessImageId = MutableLiveData<Int>()
    val guessImageId: LiveData<Int>
        get() = _guessImageId

    private val _numberOfImageOccurrences = MutableLiveData<Int>()
    val numberOfImageOccurrences: LiveData<Int>
        get() = _numberOfImageOccurrences

    private val _difficulty = MutableLiveData<Difficulty>()
    val difficulty: LiveData<Difficulty>
        get() = _difficulty

    private fun getRandomPicturesId(vectorList: List<Int>): MutableList<Int> {
        // do not show the same image twice in a row
        val tempImageIdList = mutableListOf<Int>()
        var prevImgId: Int? = null
        for (i in 0 until MAX_REPETITIONS) {
            var randomPictureId: Int
            if (i == 0) {
                randomPictureId = vectorList.random()
            }
            else {
                do {
                    randomPictureId = vectorList.random()
                } while (randomPictureId == prevImgId)
            }
            tempImageIdList.add(randomPictureId)
            prevImgId = randomPictureId
        }
        return tempImageIdList
    }

    fun initializeGame(vectorList: List<Int>) {
        _imageIdsToShow.value = getRandomPicturesId(vectorList)
        _guessImageId.value = _imageIdsToShow.value!!.random()
        _numberOfImageOccurrences.value = _imageIdsToShow.value!!.count { it == _guessImageId.value }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _difficulty.value = difficulty

        when (_difficulty.value) {
            Difficulty.EASY -> MAX_REPETITIONS = 10
            Difficulty.NORMAL -> MAX_REPETITIONS = 15
            Difficulty.HARD -> MAX_REPETITIONS = 20
        }
    }

}