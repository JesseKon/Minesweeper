/*
 TODO: Timer doesn't start as it should
 TODO: Open surrounding locations where there is 0 bombs around
 */

package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class Game : AppCompatActivity() {

    private var markBombs = false
    private var firstLocation = true

    /* ******************************************* */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Start game and timer
        startGame()
    }


    /* ******************************************* */
    // Toggle between mark bomb mode and open location mode
    fun toggleMarkBombs(view: View) {
        markBombs = !markBombs
        val button = findViewById<Button>(R.id.mark_bombs)
        var text = "Mark bombs "
        if (markBombs) text += "on" else text += "off"
        button.text = text
    }


    /* ******************************************* */
    // Randomize bomb locations
    private fun randomizeBombLocations (numOfBombs: Int, gridWidth: Int, gridHeight: Int): Array<Array<Int>> {

        // Init array
        var bombLocations = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            bombLocations += array
        }

        // Put values
        var bombsPlanted = 0
        while (bombsPlanted < numOfBombs) {
            val rndPosY = Random.nextInt(0, gridHeight - 1)
            val rndPosX = Random.nextInt(0, gridWidth - 1)
            if (bombLocations[rndPosY][rndPosX] == 0) {
                bombLocations[rndPosY][rndPosX] = 1
                ++bombsPlanted
            }
        }
        return bombLocations
    }


    /* ******************************************* */
    // Bomb was revealed, game lost
    private fun itWasBomb(button: Button, gridWidth: Int, gridHeight: Int, bombLocations: Array<Array<Int>>) {
        button.text = "PUM"
        button.textSize = 6f
        button.setBackgroundColor(0x33ff0000)
        val timer = SystemClock.elapsedRealtime() - timer.base

        // Reveal all remaining bombs
        for (y in 0..(gridHeight - 1)) {
            for (x in 0..(gridWidth - 1)) {
                if (bombLocations[y][x] == 1) {
                    val button = findViewById<Button>(y * gridHeight + x)
                    button.text = "PUM"
                    button.textSize = 6f
                    button.setBackgroundColor(0x33ff0000)
                }
            }
        }


        // Wait 1 second and goto game over with message "Game lost!"
        val intentThis = this
        Timer("Schedule", false).schedule(2000) {
            val message = "Game lost!"
            val intent = Intent(intentThis, GameOver::class.java).apply {
                putExtra("MSG", message)
                putExtra("WAS_GAME_WON", false)
                putExtra("TIMER", timer)
            }
            startActivity(intent)
        }
    }


    /* ******************************************* */
    // Returns number of bombs around at given posX, posY
    private fun howManyBombsAround(posX: Int, posY: Int, gridWidth: Int, gridHeight: Int,
                           bombLocations: Array<Array<Int>>): Int {
        var numOfBombsAround = 0

        if (posY - 1 >= 0                                   && bombLocations[posY - 1][posX]        == 1) ++numOfBombsAround // top
        if (posY - 1 >= 0 && posX + 1 < gridWidth           && bombLocations[posY - 1][posX + 1]    == 1) ++numOfBombsAround // top right
        if (posX + 1 < gridWidth                            && bombLocations[posY][posX + 1]        == 1) ++numOfBombsAround // right
        if (posY + 1 < gridHeight && posX + 1 < gridWidth   && bombLocations[posY + 1][posX + 1]    == 1) ++numOfBombsAround // bottom right
        if (posY + 1 < gridHeight                           && bombLocations[posY + 1][posX]        == 1) ++numOfBombsAround // bottom
        if (posY + 1 < gridHeight && posX - 1 >= 0          && bombLocations[posY + 1][posX - 1]    == 1) ++numOfBombsAround // bottom left
        if (posX - 1 >= 0                                   && bombLocations[posY][posX - 1]        == 1) ++numOfBombsAround // left
        if (posY - 1 >= 0 && posX - 1 >= 0                  && bombLocations[posY - 1][posX - 1]    == 1) ++numOfBombsAround // top left

        return numOfBombsAround
    }


    /* ******************************************* */
    // Create and start game
    private fun startGame() {

        // Constants
        val gridWidth = 10      // Grid width in positions
        val gridHeight = 10     // Grid height in position
        val numOfBombs = 10     // Number of bombs to plant in the grid

        // Create timer
        val timer = findViewById<Chronometer>(R.id.timer)
        timer.stop()

        // Number of locations player has opened
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
        bombLocations = randomizeBombLocations(numOfBombs, gridWidth, gridHeight)

        // Create and init array of opened locations
        var locationsOpened = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            locationsOpened += array
        }

        // Create and init locked locations
        var lockedLocations = arrayOf<Array<Int>>()
        for (y in 0..(gridHeight - 1)) {
            var array = arrayOf<Int>()
            for (x in 0..(gridWidth - 1)) {
                array += 0
            }
            lockedLocations += array
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

                // OnClick listener, this is where the action happens
                button.setOnClickListener {
                    val posY = button.id / gridWidth
                    val posX = button.id % gridWidth

                    // First opened location, there shouldn't be bombs around and it itself should not be a bomb
                    if (firstLocation) {
                        while (howManyBombsAround(posX, posY, gridWidth, gridHeight, bombLocations) != 0
                            || bombLocations[posY][posX] == 1)
                            bombLocations = randomizeBombLocations(numOfBombs, gridWidth, gridHeight)

                        timer.start() // Start timer
                        firstLocation = false
                    }

                    // Lock and unlock potential bomb locations in "mark bombs"-mode
                    if (markBombs && locationsOpened[posY][posX] != 1) {
                        if (lockedLocations[posY][posX] == 0) {
                            button.text = "X"
                            lockedLocations[posY][posX] = 1
                        }
                        else {
                            button.text = ""
                            lockedLocations[posY][posX] = 0
                        }
                        return@setOnClickListener
                    }

                    // Was it a bomb?
                    if (lockedLocations[posY][posX] != 1 && bombLocations[posY][posX] == 1)
                        itWasBomb(button, gridWidth, gridHeight, bombLocations)

                    // Not a bomb, how many bombs around?
                    if (lockedLocations[posY][posX] != 1 && bombLocations[posY][posX] != 1) {
                        val numOfBombsAround = howManyBombsAround(posX, posY, gridWidth, gridHeight, bombLocations)

                        button.text = numOfBombsAround.toString()
                        button.setBackgroundColor(0x3300ff00)

                        if (locationsOpened[posY][posX] == 0) {
                            locationsOpened[posY][posX] = 1
                            ++numOfLocationsOpened

                            // Was the game won?
                            if (numOfLocationsOpened >= numOfLocationsToOpenForTheWin) {
                                val message = "Game won!"
                                val timer = SystemClock.elapsedRealtime() - timer.base
                                val intentThis = this

                                // Wait 1 second and goto game over with message "Game won!"
                                Timer("Schedule", false).schedule(500) {
                                    val intent = Intent(intentThis, GameOver::class.java).apply {
                                        putExtra("MSG", message)
                                        putExtra("WAS_GAME_WON", true)
                                        putExtra("TIMER", timer)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }

                linearLayout.addView(button)
            }
        }
    }
}
