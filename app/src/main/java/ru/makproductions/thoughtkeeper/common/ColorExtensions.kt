package ru.makproductions.thoughtkeeper.common

import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.entity.Color

fun Color.toResource() = when (this) {
    Color.WHITE -> R.color.white
    Color.RED -> R.color.red
    Color.GREEN -> R.color.green
    Color.BLUE -> R.color.blue
    Color.PINK -> R.color.pink
    Color.VIOLET -> R.color.violet
    Color.YELLOW -> R.color.yellow
}

fun String.toColor(): Color = when (this) {
    "RED" -> Color.RED
    "WHITE" -> Color.WHITE
    "BLUE" -> Color.BLUE
    "PINK" -> Color.PINK
    "VIOLET" -> Color.VIOLET
    "YELLOW" -> Color.YELLOW
    "GREEN" -> Color.GREEN
    else -> Color.WHITE
}