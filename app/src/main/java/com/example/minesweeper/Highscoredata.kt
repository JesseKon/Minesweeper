package com.example.minesweeper

import android.content.Context
import android.content.SharedPreferences

class Highscoredata {

    val PREFS_NAME = "MinesweeperHighScores"

    fun set(context: Context, score: Long) {
        val highScores = Array<Long>(6, { 0 })
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        highScores[0] = prefs.getLong("1", 999)
        highScores[1] = prefs.getLong("2", 999)
        highScores[2] = prefs.getLong("3", 999)
        highScores[3] = prefs.getLong("4", 999)
        highScores[4] = prefs.getLong("5", 999)
        highScores[5] = score
        highScores.sort()

        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putLong("1", highScores[0])
        editor.putLong("2", highScores[1])
        editor.putLong("3", highScores[2])
        editor.putLong("4", highScores[3])
        editor.putLong("5", highScores[4])

        editor.commit()
    }

    fun get(context: Context): Array<Long> {
        val highScores = Array<Long>(5, { 0 })

        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        highScores[0] = prefs.getLong("1", 999)
        highScores[1] = prefs.getLong("2", 999)
        highScores[2] = prefs.getLong("3", 999)
        highScores[3] = prefs.getLong("4", 999)
        highScores[4] = prefs.getLong("5", 999)

        return highScores
    }

}