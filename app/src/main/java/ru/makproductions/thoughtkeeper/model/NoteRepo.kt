package ru.makproductions.thoughtkeeper.model

import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.FirestoreProvider
import ru.makproductions.thoughtkeeper.model.provider.RemoteDataProvider

object NoteRepo {
    private val remoteProvider: RemoteDataProvider = FirestoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}