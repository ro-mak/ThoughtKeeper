package ru.makproductions.thoughtkeeper.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.view.note.adapter.NotesAdapter
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        notes_recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter()
        notes_recycler_view.adapter = adapter
        viewModel.viewState()
            .observe(this, Observer { viewstate ->
                viewstate?.let {
                    adapter.notes = viewstate.notes
                    adapter.notifyDataSetChanged()
                }
            })
        add_note_fab.setOnClickListener { viewModel.addNote() }
    }
}
