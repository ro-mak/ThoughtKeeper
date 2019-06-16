package ru.makproductions.thoughtkeeper.viewmodel.note

import kotlinx.coroutines.launch
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import timber.log.Timber

class NoteViewModel(private val noteRepo: NoteRepo) : BaseViewModel<NoteData>() {

    private val pendingNote: Note?
        get() = getViewState().poll()?.note


    fun saveNote(note: Note) {
        Timber.e("save note")
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        Timber.e("load note")
        launch {
            try {
                setData(NoteData(note = noteRepo.getNoteById(noteId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    override fun onCleared() {
        Timber.e("OnCleared")
        launch {
            pendingNote?.let { note -> noteRepo.saveNote(note) }

            super.onCleared()
        }
    }

    fun deleteNote() {
        launch {
            try {
                pendingNote?.let { noteRepo.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))

            } catch (e: Throwable) {
                setError(e)
            }
        }

    }
}
