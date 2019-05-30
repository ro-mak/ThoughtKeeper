package ru.makproductions.thoughtkeeper.model.provider

import android.arch.lifecycle.LiveData
import ru.makproductions.thoughtkeeper.model.entity.Note

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}