package ru.makproductions.thoughtkeeper.viewmodel.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note

class MainViewModel : ViewModel() {
    val defaultNoteList: MutableList<Note>? = null
    private val noteRepo: NoteRepo = NoteRepo(defaultNoteList)
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(noteRepo.loadNotes())
    }

    fun saveNotes(notes: MutableList<Note>) {
        noteRepo.saveNotes(notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
    fun addNote() {
        viewStateLiveData.value?.notes?.add(Note("New Note", "Place your text here"))
        viewStateLiveData.postValue(MainViewState(viewStateLiveData.value?.notes))
    }

    override fun onCleared() {
        super.onCleared()
        viewStateLiveData.value?.let { value ->
            value.notes?.let { notes -> saveNotes(notes) }
        }

    }
}
