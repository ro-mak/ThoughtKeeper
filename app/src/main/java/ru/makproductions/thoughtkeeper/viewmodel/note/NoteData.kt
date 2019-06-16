package ru.makproductions.thoughtkeeper.viewmodel.note

import ru.makproductions.thoughtkeeper.model.entity.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
