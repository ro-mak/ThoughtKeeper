package ru.makproductions.thoughtkeeper.model

import ru.makproductions.thoughtkeeper.model.entity.Note

data class NoteRepo(var notes: MutableList<Note>?) {

    fun saveNotes(notes: MutableList<Note>) {
        this.notes?.let { this.notes?.addAll(notes) }.let { this.notes = notes }
    }

    fun loadNotes(): MutableList<Note> {
        val noteList = mutableListOf(Note("1", "text 1"), Note("2", "text 2"), Note("3", "text 3"))
        return noteList
    }
}