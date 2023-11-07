package kr.ac.kumoh.ce.s20180727w04carddealer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData<IntArray>(IntArray(5) { -1 })
    private var _handRank: MutableLiveData<String> = MutableLiveData()
    val cards: LiveData<IntArray>
        get() = _cards
    val handRank: LiveData<String>
        get() = _handRank
    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) { -1 }

        for (i in newCards.indices) {
            // 중복 검사
            do {
                num = Random.nextInt(52)
            } while (newCards.contains(num))
            newCards[i] = num
        }
        newCards.sort()
        _cards.value = newCards
    }

    fun determineHandRank() {
        _handRank.value = when {
            isRoyalStraightFlush() ->  "로얄 스트레이트 플러쉬"
            isBackStraightFlush() -> "백스트레이트 플러쉬"
            isStraightFlush() -> "스트레이트 플러쉬"
            isFourCards() -> "포카드"
            isFullHouse() -> "풀하우스"
            isFlush() -> "플러시"
            isMountain() -> "마운틴"
            isBackStraight() -> "백스트레이트"
            isStraight() -> "스트레이트"
            isTriple() -> "트리플"
            isTwoPair() -> "투페어"
            isPair() -> "원페어"
            else -> "탑"
        }
    }

    private fun isPair(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        return numbers?.toSet()?.size == 4
    }
    private fun isTwoPair(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        return numbers?.toSet()?.size == 3
    }
    private fun isTriple(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        for (i in numbers!!) {
            if (numbers.count { it == i } == 3) return true
        }
        return false
    }
    private fun isFourCards(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        for (i in numbers!!) {
            if (numbers.count { it == i } == 4) return true
        }
        return false
    }
    private fun isBackStraight(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        return numbers == intArrayOf(0, 1, 2, 3, 4).toList()
    }
    private fun isFlush(): Boolean {
        val numbers = _cards.value!!.map { it/13 }.toList().sorted()
        return numbers.all { it == numbers[0] }
    }
    private fun isMountain(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        return numbers == intArrayOf(0, 9, 10, 11, 12).toList()
    }
    private fun isStraight(): Boolean {
        val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
        for (i in 0 until 4) {
            if (numbers?.get(i)!!+1 != numbers[i+1]) return false
        }
        return true
    }
    private fun isFullHouse(): Boolean {
        if (isTriple()) {
            val numbers = _cards.value?.map { it%13 }?.toList()?.sorted()
            if (numbers?.toSet()?.size == 2) {
                return true
            }
        }
        return false
    }
    private fun isStraightFlush(): Boolean {
        return isFlush() && isStraight()
    }
    private fun isBackStraightFlush(): Boolean {
        return isFlush() && isBackStraight()
    }
    private fun isRoyalStraightFlush(): Boolean {
        return isFlush() && isMountain()
    }
}

