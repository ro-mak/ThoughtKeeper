package ru.makproductions.thoughtkeeper.model.provider

import android.arch.lifecycle.LiveData
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.entity.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}