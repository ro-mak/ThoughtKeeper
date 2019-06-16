package ru.makproductions.thoughtkeeper.model.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.entity.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(id: String)
}