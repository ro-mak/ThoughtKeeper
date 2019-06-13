package ru.makproductions.thoughtkeeper.model.provider

sealed class NoteResult {
    data class NoteLoadSuccess<out T>(val data: T) : NoteResult()
    data class NoteLoadError(val throwable: Throwable?) : NoteResult()
}