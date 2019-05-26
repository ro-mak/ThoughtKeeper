package ru.makproductions.thoughtkeeper.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(var title: String, var text: String, var color: Int = 0xFFFFFF.toInt()) : Parcelable

enum class Color {
    RED,
    WHITE,
    GREEN,
    YELLOW,
    BLUE,
    VIOLET,
    BLACK
}