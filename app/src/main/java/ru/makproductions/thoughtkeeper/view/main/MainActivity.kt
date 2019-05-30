package ru.makproductions.thoughtkeeper.view.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseActivity
import ru.makproductions.thoughtkeeper.view.note.NoteActivity
import ru.makproductions.thoughtkeeper.view.note.adapter.NotesAdapter
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewModel
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewState

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    lateinit var adapter: NotesAdapter
    override val layoutRes: Int
        get() = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notes_recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter { NoteActivity.start(this, it.id) }
        notes_recycler_view.adapter = adapter
        add_note_fab.setOnClickListener { viewModel.addNote() }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }
}
