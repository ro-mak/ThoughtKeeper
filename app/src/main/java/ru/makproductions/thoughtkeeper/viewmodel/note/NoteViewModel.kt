package ru.makproductions.thoughtkeeper.viewmodel.note

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import timber.log.Timber

class NoteViewModel(private val noteRepo: NoteRepo) : BaseViewModel<NoteData>() {


    init {
        viewStateLiveData.value = NoteData()
    }

    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    fun saveNote(note: Note) {
        setData(NoteData(note = note))
    }

    private var repositoryNote: LiveData<NoteResult>? = null

    fun loadNote(noteId: String) {
        launch {
            try {
                noteRepo.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
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
