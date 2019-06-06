package ru.makproductions.thoughtkeeper.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(var id: String = "", var title: String = "", var text: String = "", var color: Color = Color.WHITE) :
    Parcelable {
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

