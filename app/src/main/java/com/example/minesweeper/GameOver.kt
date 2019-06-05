package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

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
        val time = intent.getStringExtra("TIMER")
        textView.text = message

        if (wasGameWon)
            timeView.text = "Time: " + time
    }

    fun backToMainMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
