package ru.makproductions.thoughtkeeper.viewmodel.main

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel
import java.util.*

class MainViewModel(val noteRepo: NoteRepo) : BaseViewModel<List<Note>?>() {

    private val notesChannel = noteRepo.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is NoteResult.NoteLoadSuccess<*> -> {
                        setData(it.data as? List<Note>)
                    }
                    is NoteResult.NoteLoadError -> {
                        setError(it.throwable)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }

    fun addNote() {
        val viewstate = getViewState()
        val list = viewstate.poll() as MutableList<Note>
        val note = Note(UUID.randomUUID().toString(), "New Note", "Place your text here")
        launch {
            noteRepo.saveNote(note)
        }
        list.add(note)
        setData(list)
    }
}
