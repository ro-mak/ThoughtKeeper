package ru.makproductions.thoughtkeeper.model

import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.RemoteDataProvider

class NoteRepo(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}