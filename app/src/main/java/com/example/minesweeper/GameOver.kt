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
        val timer = intent.getLongExtra("TIMER", 0) / 1000

        textView.text = message
        timeView.text = "Time: " + timer.toString()

        val scoresObj = Highscoredata()

        if (wasGameWon)
            scoresObj.set(this, timer)

        val scoresTxt = findViewById<TextView>(R.id.scores)
        val scores = scoresObj.get(this)

        var output: String = "Highscores:\n"
        var it = 0
        for (score in scores)
            output += (++it).toString() + ". " + score.toString() + "\n"
        scoresTxt.text = output
    }

    fun backToMainMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
