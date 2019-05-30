package ru.makproductions.thoughtkeeper.viewmodel.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import timber.log.Timber

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.NoteLoadSuccess<*> -> {
                    viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                }
                is NoteResult.NoteLoadError -> {
                    viewStateLiveData.value = NoteViewState(error = t.throwable)
                }
            }
        }
    }

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null
    fun saveNote(note: Note) {
        pendingNote = note
    }

    private var repositoryNote: LiveData<NoteResult>? = null
    fun loadNote(noteId: String) {
        repositoryNote = NoteRepo.getNoteById(noteId)
        repositoryNote!!.observeForever(noteObserver)
    }

    override fun onCleared() {
        Timber.e("OnCleared")
        super.onCleared()
        pendingNote?.let { note -> NoteRepo.saveNote(note) }
        repositoryNote?.let { it.removeObserver(noteObserver) }

    }
}
