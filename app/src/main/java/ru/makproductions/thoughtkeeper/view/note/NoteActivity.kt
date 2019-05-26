package ru.makproductions.thoughtkeeper.view.note

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_note.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.viewmodel.note.NoteViewModel

class NoteActivity : AppCompatActivity() {

    companion object {

        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, note: Note? = null) {
            context.startActivity(Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(
                        EXTRA_NOTE,
                        note
                    )
                }
            })
        }
    }

    private var note: Note? = null
    lateinit var viewModel: NoteViewModel
    val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(note_activity_toolbar)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        note = intent.getParcelableExtra(EXTRA_NOTE)
        initNote()
    }

    private fun initNote() {
        note?.let { note ->
            note_activity_title_edit_text.setText(note.title)
            note_activity_content_edit_text.setText(note.text)
        }
    }

    private fun saveNote() {
        if (note_activity_content_edit_text.text != null || note_activity_title_edit_text.text!!.length < 3) return
        note = note?.copy(
            title = note_activity_title_edit_text.text.toString(),
            text = note_activity_content_edit_text.text.toString()
        ) ?: createNewNote()
        if (note != null) viewModel.saveNote(note!!)
    }

    fun createNewNote() =
        Note(note_activity_title_edit_text.text.toString(), note_activity_content_edit_text.text.toString())
}