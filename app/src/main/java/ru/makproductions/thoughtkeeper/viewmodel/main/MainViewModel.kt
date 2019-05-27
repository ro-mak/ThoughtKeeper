package ru.makproductions.thoughtkeeper.viewmodel.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import java.util.*

class MainViewModel : ViewModel() {
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        NoteRepo.getNotesLiveData().observeForever({
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it) ?: MainViewState(it)
        })

    }

    fun saveNotes(notes: MutableList<Note>) {
        NoteRepo.saveNotes(notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
    fun addNote() {
        viewStateLiveData.value?.notes?.add(Note(UUID.randomUUID().toString(), "New Note", "Place your text here"))
        viewStateLiveData.postValue(MainViewState(viewStateLiveData.value?.notes))
    }

    override fun onCleared() {
        super.onCleared()
        viewStateLiveData.value?.let { value ->
            value.notes?.let { notes -> saveNotes(notes) }
        }

    }
}
