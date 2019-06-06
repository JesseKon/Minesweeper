package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class Highscores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscores)
        displayScores()
    }

    private fun displayScores() {
        val scoresObj = Highscoredata()
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
