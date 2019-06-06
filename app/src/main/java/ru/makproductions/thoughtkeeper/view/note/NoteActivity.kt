package ru.makproductions.thoughtkeeper.view.note

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.ajalt.timberkt.Timber
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.common.toColor
import ru.makproductions.thoughtkeeper.common.toResource
import ru.makproductions.thoughtkeeper.model.entity.Color
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseActivity
import ru.makproductions.thoughtkeeper.viewmodel.note.NoteViewModel
import ru.makproductions.thoughtkeeper.viewmodel.note.NoteViewState
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    companion object {

        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, noteId: String? = null) {
            context.startActivity(Intent(context, NoteActivity::class.java).apply {
                noteId?.let {
                    putExtra(
                        EXTRA_NOTE,
                        noteId
                    )
                }
            })
        }
    }

    private var noteId: String? = null
    private var note: Note? = null
    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }
    override val layoutRes: Int
        get() = R.layout.activity_note
    val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
            Timber.e(message = { "afterTextChanged" })
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(note_activity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { viewModel.loadNote(it) }
            ?: let {
                supportActionBar?.title = getString(R.string.new_note_title)
            }
        note_color_spinner.adapter =
            ArrayAdapter<String>(this, R.layout.color_spinner_item, resources.getStringArray(R.array.colors))
        note_color_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                note_activity_toolbar.setBackgroundColor(note_color_spinner.adapter.getItem(position).toString().toColor().toResource())
                saveNote()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) {
            finish()
            return
        }
        this.note = data.note
        supportActionBar?.title = note?.title ?: getString(R.string.new_note_title)
        initNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        MenuInflater(this).inflate(R.menu.note_options_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
            android.R.id.home -> super.onBackPressed().let { true }
            R.id.note_delete_button -> deleteNote().let { true }
            else -> super.onOptionsItemSelected(item)
        }

    private fun deleteNote() {
        alert {
            messageResource = R.string.delete_note_alert_message
            negativeButton(R.string.delete_note_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.delete_note_ok) { viewModel.deleteNote() }
        }.show()
    }

    private fun initNote() {
        note?.let { note ->
            removeTextListener()
            note_activity_title_edit_text.setText(note.title)
            note_activity_content_edit_text.setText(note.text)
            val color = when (note.color) {
                Color.WHITE -> R.color.white
                Color.RED -> R.color.red
                Color.GREEN -> R.color.green
                Color.BLUE -> R.color.blue
                Color.PINK -> R.color.pink
                Color.VIOLET -> R.color.violet
                Color.YELLOW -> R.color.yellow
            }
            note_activity_toolbar.setBackgroundColor(color)
        }
        setTextListener()
    }

    private fun setTextListener() {
        note_activity_content_edit_text.addTextChangedListener(textWatcher)
        note_activity_title_edit_text.addTextChangedListener(textWatcher)
    }

    private fun removeTextListener() {
        note_activity_content_edit_text.removeTextChangedListener(textWatcher)
        note_activity_title_edit_text.removeTextChangedListener(textWatcher)
    }


    private fun saveNote() {
        Timber.e(message = { "saveNote" })
        if (note_activity_content_edit_text.text == null || note_activity_title_edit_text.text!!.length < 3) return
        removeTextListener()
        note = note?.copy(
            title = note_activity_title_edit_text.text.toString(),
            text = note_activity_content_edit_text.text.toString(),
            color = note_color_spinner.selectedItem.toString().toColor()
        ) ?: createNewNote()
        note?.let { viewModel.saveNote(it) }
        setTextListener()
    }


    fun createNewNote() =
        Note(
            UUID.randomUUID().toString(),
            note_activity_title_edit_text.text.toString(),
            note_activity_content_edit_text.text.toString()
        )
}