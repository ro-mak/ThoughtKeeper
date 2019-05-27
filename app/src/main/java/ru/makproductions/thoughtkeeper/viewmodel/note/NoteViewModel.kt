package ru.makproductions.thoughtkeeper.viewmodel.note

import android.arch.lifecycle.ViewModel
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import timber.log.Timber

class NoteViewModel : ViewModel() {

    private var pendingNote: Note? = null
    fun saveNote(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        Timber.e("OnCleared")
        super.onCleared()
        pendingNote?.let { note -> NoteRepo.saveNote(note) }
    }
}