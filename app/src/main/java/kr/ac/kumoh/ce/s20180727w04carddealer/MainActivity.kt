package kr.ac.kumoh.ce.s20180727w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180727w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in it.indices) {
                res[i] = resources.getIdentifier(
                    getCardName(it[i]),
                    "drawable",
                    packageName
                )
            }

            main.card1.setImageResource(res[0])
            main.card2.setImageResource(res[1])
            main.card3.setImageResource(res[2])
            main.card4.setImageResource(res[3])
            main.card5.setImageResource(res[4])
        })

        model.handRank.observe(this) {
            main.rank.text = model.handRank.value
        }
        main.btnShuffle.setOnClickListener {
            model.shuffle()
            model.determineHandRank()
        }

        main.btnSimulator?.setOnClickListener {
            val result = IntArray(13) { 0 }
            var simulationsRun = 0
            val handler = Handler(Looper.getMainLooper())

            // 셔플과 족보 판별을 반복적으로 수행하는 Runnable 객체
            val simulationRunnable = object : Runnable {
                override fun run() {
                    if (simulationsRun < 10000) {
                        model.shuffle()
                        model.determineHandRank()
                        simulationsRun++
                        handler.postDelayed(this, 1)
                    } else {
                        val resultText = result.withIndex()
                            .joinToString(separator = "\n") { (index, count) ->
                                "${getHandRankString(index)}: ${count}회  /  ${String.format("%.2f", count / 10000.0 * 100)}%"
                            }

                        AlertDialog.Builder(this@MainActivity).apply {
                            setTitle("시뮬레이션 결과")
                            setMessage(resultText)
                            setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                            show()
                        }
                    }
                }
            }

            val handRankObserver = Observer<String> { handRank ->
                when (handRank) {
                    "로얄 스트레이트 플러쉬" -> result[0]++
                    "백스트레이트 플러쉬" -> result[1]++
                    "스트레이트 플러쉬" -> result[2]++
                    "포카드" -> result[3]++
                    "풀하우스" -> result[4]++
                    "플러시" -> result[5]++
                    "마운틴" -> result[6]++
                    "백스트레이트" -> result[7]++
                    "스트레이트" -> result[8]++
                    "트리플" -> result[9]++
                    "투페어" -> result[10]++
                    "원페어" -> result[11]++
                    "탑" -> result[12]++
                    else -> ""
                }
            }
            // Observer 등록
            model.handRank.observe(this, handRankObserver)

            // 최초의 시뮬레이션 시작
            handler.post(simulationRunnable)
        }
    }

    private fun getCardName(c: Int) : String {
        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "error"
        }

        val number = when (c % 13) {
            -1->"joker"
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "error"
        }
        if(number == "joker")
            return "c_red_joker"
        return "c_${number}_of_${shape}"
    }

    private fun getHandRankString(index: Int): String {
        return when (index) {
            0 -> "로얄 스트레이트 플러쉬"
            1 -> "백스트레이트 플러쉬"
            2 -> "스트레이트 플러쉬"
            3 -> "포카드"
            4 -> "풀하우스"
            5 -> "플러시"
            6 -> "마운틴"
            7 -> "백스트레이트"
            8 -> "스트레이트"
            9 -> "트리플"
            10 -> "투페어"
            11 -> "원페어"
            12 -> "탑"
            else -> "알 수 없음"
        }
    }

}

