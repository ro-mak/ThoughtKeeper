package ru.makproductions.thoughtkeeper.viewmodel.main

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel

class MainViewModel(noteRepo: NoteRepo) : BaseViewModel<List<Note>?>() {

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
}
