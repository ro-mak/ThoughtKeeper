package ru.makproductions.thoughtkeeper.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.makproductions.thoughtkeeper.R

@Parcelize
data class Note(var id: String, var title: String, var text: String, var color: Color = Color.WHITE) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }
}

enum class Color {
    RED,
    WHITE,
    GREEN,
    YELLOW,
    BLUE,
    VIOLET,
    PINK
}

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