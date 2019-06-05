package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class Game : AppCompatActivity() {

    /* ******************************************* */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        startGame()
    }

    /* ******************************************* */
    private fun startGame() {

        // Constants
        val gridWidth = 10
        val gridHeight = 10
        val numOfBombs = 10

        // Init array
        var bombLocations = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            bombLocations += array
        }

        // Randomize bomb locations
        var bombsPlanted = 0
        while (bombsPlanted < numOfBombs) {
            val randomIndexY = Random.nextInt(0, gridHeight - 1)
            val randomIndexX = Random.nextInt(0, gridWidth - 1)
            if (bombLocations[randomIndexY][randomIndexX] == 0) {
                bombLocations[randomIndexY][randomIndexX] = 666
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
                    }
                }

                linearLayout.addView(button)
            }
        }
    }
}
