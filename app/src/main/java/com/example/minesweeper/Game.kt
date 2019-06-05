/*
 TODO: First location should never be a bomb
 TODO: Open surrounding locations where there is 0 bombs around
 TODO: Save best times
 */

package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.random.Random

class Game : AppCompatActivity() {

    /* ******************************************* */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Create timer
        val timer = findViewById<Chronometer>(R.id.timer)

        // Start game and timer
        startGame()
        timer.start()

    }

    /* ******************************************* */
    private fun startGame() {

        // Constants
        val gridWidth = 10
        val gridHeight = 10
        val numOfBombs = 10

        var numOfLocationsOpened = 0
        val numOfLocationsToOpenForTheWin = gridWidth * gridHeight - numOfBombs

        // Create and init bomb array
        var bombLocations = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            bombLocations += array
        }

        // Create and init array of opened locations
        var locationsOpened = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            locationsOpened += array
        }

        // Randomize bomb locations
        var bombsPlanted = 0
        while (bombsPlanted < numOfBombs) {
            val rndPosY = Random.nextInt(0, gridHeight - 1)
            val rndPosX = Random.nextInt(0, gridWidth - 1)
            if (bombLocations[rndPosY][rndPosX] == 0) {
                bombLocations[rndPosY][rndPosX] = 666
                ++bombsPlanted
            }
        }


        // Calculate screen width in pixels
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val buttonWidth = width / gridWidth
        val buttonHeight = width / gridHeight

        // Create buttons
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        for (y in 0..(gridHeight - 1)) {
            val linearLayout = LinearLayout(this)

            linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setPadding(0, y * buttonHeight, 0, 0)

            constraintLayout.addView(linearLayout)

            for (x in 0..(gridWidth - 1)) {
                val button = Button(this)

                button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val params = button.layoutParams
                params.width = buttonWidth
                params.height = buttonHeight
                button.layoutParams = params
                button.id = y * gridHeight + x
                button.textSize = buttonWidth / 10f

                button.setOnClickListener {
                    val posY = button.id / gridWidth
                    val posX = button.id % gridWidth

                    // Was it a bomb?
                    if (bombLocations[posY][posX] == 666) {
                        button.text = "PUM"
                        button.setBackgroundColor(0x33ff0000)

                        Thread.sleep(1000)

                        // Go to game over
                        val message = "Game lost!"
                        val intent = Intent(this, GameOver::class.java).apply {
                            putExtra("MSG", message)
                            putExtra("WAS_GAME_WON", false)
                            putExtra("TIMER_STRING", timer.text.toString())
                        }
                        startActivity(intent)
                    }

                    // How many bombs around?
                    else {
                        var numOfBombsAround = 0

                        // top
                        if (posY - 1 >= 0 && bombLocations[posY - 1][posX] == 666) {
                            ++numOfBombsAround
                        }

                        // top right
                        if (posY - 1 >= 0 && posX + 1 < gridWidth && bombLocations[posY - 1][posX + 1] == 666) {
                            ++numOfBombsAround
                        }

                        // right
                        if (posX + 1 < gridWidth && bombLocations[posY][posX + 1] == 666) {
                            ++numOfBombsAround
                        }

                        // bottom right
                        if (posY + 1 < gridHeight && posX + 1 < gridWidth && bombLocations[posY + 1][posX + 1] == 666) {
                            ++numOfBombsAround
                        }

                        // bottom
                        if (posY + 1 < gridHeight && bombLocations[posY + 1][posX] == 666) {
                            ++numOfBombsAround
                        }

                        // bottom left
                        if (posY + 1 < gridHeight && posX - 1 >= 0 && bombLocations[posY + 1][posX - 1] == 666) {
                            ++numOfBombsAround
                        }

                        // left
                        if (posX - 1 >= 0 && bombLocations[posY][posX - 1] == 666) {
                            ++numOfBombsAround
                        }

                        // top left
                        if (posY - 1 >= 0 && posX - 1 >= 0 && bombLocations[posY - 1][posX - 1] == 666) {
                            ++numOfBombsAround
                        }

                        button.text = numOfBombsAround.toString()
                        button.setBackgroundColor(0x3300ff00)

                        if (locationsOpened[posY][posX] == 0) {
                            locationsOpened[posY][posX] = 1
                            ++numOfLocationsOpened

                            // Was the game won?
                            if (numOfLocationsOpened >= numOfLocationsToOpenForTheWin) {
                                val message = "Game won!"
                                val timeItTook = SystemClock.elapsedRealtime() - timer.base
                                val intent = Intent(this, GameOver::class.java).apply {
                                    putExtra("MSG", message)
                                    putExtra("WAS_GAME_WON", true)
                                    putExtra("TIMER_STRING", timer.text.toString())
                                    putExtra("TIMER_INT", timeItTook)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }

                linearLayout.addView(button)
            }
        }
    }
}
