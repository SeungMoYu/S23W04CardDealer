package kr.ac.kumoh.ce.s20180727w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        main.btnShuffle.setOnClickListener {
            model.shuffle()
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
}