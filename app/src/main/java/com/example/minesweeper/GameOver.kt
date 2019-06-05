package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.minesweeper.Highscoredata
import kotlinx.android.synthetic.main.activity_game.*

class GameOver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        displayMessage()
    }

    private fun displayMessage() {
        val textView = findViewById<TextView>(R.id.game_over)
        val timeView = findViewById<TextView>(R.id.message)
        val message = intent.getStringExtra("MSG")
        val wasGameWon = intent.getBooleanExtra("WAS_GAME_WON", false)
        val timerString = intent.getStringExtra("TIMER_STRING")
        val timerLong = intent.getLongExtra("TIMER_INT", 0)
        textView.text = message

        Log.e("Timer", timerLong.toString())

        if (wasGameWon)
            timeView.text = "Time: " + timerString

        val scoresTxt = findViewById<TextView>(R.id.scores)
        val scoresObj = Highscoredata()
        val scores = scoresObj.get(this)
        var output: String = ""
        for (score in scores) {
            output += score.toString() + "\n"
        }
        scoresTxt.text = output

    }

    fun backToMainMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
