package ru.makproductions.thoughtkeeper.model

import android.content.Context
import ru.makproductions.thoughtkeeper.App
import ru.makproductions.thoughtkeeper.model.entity.Note
import java.util.*

data class NoteRepo(var notes: List<Note>) {

    val prefKey = "NOTE_PREFS"
    val prefTextKey = "NOTES_TEXT"
    val prefTitleKey = "NOTES_TITLE"
    fun saveNotes(notes: List<Note>) {
        this.notes = notes
        val prefs = App.instance.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val noteTextStringSet = TreeSet<String>()
        val noteTitleStringSet = TreeSet<String>()
        for (note in notes) {
            noteTextStringSet.add(note.text)
            noteTitleStringSet.add(note.title)
        }
        editor.putStringSet(prefTitleKey, noteTitleStringSet)
        editor.putStringSet(prefTextKey, noteTextStringSet)
        editor.apply()
    }

    fun loadNotes() {
        val prefs = App.instance.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val noteTextStringSet = prefs.getStringSet(prefTextKey, setOf("No notes"))?.toList()
        val noteTitleStringSet = prefs.getStringSet(prefTitleKey, setOf("No titles"))?.toList()
        val noteList = ArrayList<Note>()
        noteTitleStringSet?.let {
            noteTextStringSet?.let {
                for (id in 0 until noteTitleStringSet.size) {
                    noteList.add(Note(noteTitleStringSet[id], noteTextStringSet[id]))
                }
            }
        }
        notes = noteList
    }
}