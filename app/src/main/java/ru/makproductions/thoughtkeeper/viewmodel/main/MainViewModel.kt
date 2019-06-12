package ru.makproductions.thoughtkeeper.viewmodel.main

import android.arch.lifecycle.Observer
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import java.util.*

class MainViewModel(noteRepo: NoteRepo) : BaseViewModel<List<Note>?, MainViewState>() {
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.NoteLoadSuccess<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.NoteLoadError -> {
                    viewStateLiveData.value = MainViewState(error = t.throwable)
                }
            }
        }
    }

    private val repositoryNotes = noteRepo.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    fun addNote() {
        val noteList = viewStateLiveData.value?.notes as MutableList<Note>
        noteList.add(Note(UUID.randomUUID().toString(), "New Note", "Place your text here"))
        viewStateLiveData.postValue(MainViewState(viewStateLiveData.value?.notes))
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}
