package ru.makproductions.thoughtkeeper.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ru.makproductions.thoughtkeeper.model.entity.Color
import ru.makproductions.thoughtkeeper.model.entity.Note
import timber.log.Timber
import java.util.*

object NoteRepo {
    private val notesLiveData = MutableLiveData<MutableList<Note>>()
    var notes = mutableListOf<Note>()

    init {
        notes = loadNotes()
        notesLiveData.value = notes
    }

    fun getNotesLiveData(): LiveData<MutableList<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        Timber.e("Save note " + note)
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun saveNotes(notes: MutableList<Note>) {
        this.notes.addAll(notes)
    }

    fun loadNotes(): MutableList<Note> {
        val noteList = mutableListOf(
            Note(UUID.randomUUID().toString(), "Note 1", "text 1", Color.YELLOW),
            Note(UUID.randomUUID().toString(), "Note 2", "text 2", Color.BLUE),
            Note(UUID.randomUUID().toString(), "Note 3", "text 3", Color.VIOLET)
        )
        return noteList
    }
}