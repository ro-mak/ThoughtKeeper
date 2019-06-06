package ru.makproductions.thoughtkeeper.viewmodel.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import timber.log.Timber

class NoteViewModel : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.NoteLoadSuccess<*> -> {
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = t.data as? Note))
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

    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data?.note
    fun saveNote(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    private var repositoryNote: LiveData<NoteResult>? = null

    fun loadNote(noteId: String) {
        repositoryNote = NoteRepo.getNoteById(noteId)
        repositoryNote!!.observeForever(noteObserver)
    }

    override fun onCleared() {
        Timber.e("OnCleared")
        pendingNote?.let { note -> NoteRepo.saveNote(note) }
        repositoryNote?.let { it.removeObserver(noteObserver) }

    }

    fun deleteNote() {
        pendingNote?.let {
            NoteRepo.deleteNote(it.id)
                .observeForever { result ->
                    result?.let {
                        viewStateLiveData.value = when (result) {
                            is NoteResult.NoteLoadSuccess<*> -> {
                                NoteViewState(NoteViewState.Data(isDeleted = true))
                            }
                            is NoteResult.NoteLoadError -> {
                                NoteViewState(error = result.throwable)
                            }
                        }
                    }
                }
        }
    }
}
