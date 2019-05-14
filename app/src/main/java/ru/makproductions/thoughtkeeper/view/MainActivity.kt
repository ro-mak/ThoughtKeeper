package ru.makproductions.thoughtkeeper.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getNote().observe(this, Observer { value ->
            value?.let {
                show_note_text_view.text = it
            }
        })
        button.setOnClickListener {
            viewModel.saveNote(note_edit_text.text.toString())
        }

    }
}
