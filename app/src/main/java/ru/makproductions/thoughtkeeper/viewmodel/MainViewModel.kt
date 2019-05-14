package ru.makproductions.thoughtkeeper.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.makproductions.thoughtkeeper.App
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.NoteRepo

class MainViewModel : ViewModel() {
    private val noteRepo: NoteRepo = NoteRepo(App.instance.getString(R.string.default_note))
    private val noteMutableLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        noteMutableLiveData.value = noteRepo.note
    }

    fun getNote(): LiveData<String> = noteMutableLiveData
    private fun changeNote() {
        noteMutableLiveData.postValue(noteRepo.note)
    }

    fun saveNote(note: String) {
        noteRepo.note = note
        changeNote()
    }
}
