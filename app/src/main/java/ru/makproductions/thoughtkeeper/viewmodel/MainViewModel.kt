package ru.makproductions.thoughtkeeper.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note

class MainViewModel : ViewModel() {
    val defaultNoteList = listOf(Note("", ""))
    private val noteRepo: NoteRepo = NoteRepo(defaultNoteList)
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        noteRepo.loadNotes()
        viewStateLiveData.value = MainViewState(noteRepo.notes)
    }

    fun saveNotes(notes: List<Note>) {
        noteRepo.saveNotes(notes)
        viewStateLiveData.value = MainViewState(notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}
