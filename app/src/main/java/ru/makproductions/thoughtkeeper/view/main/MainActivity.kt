package ru.makproductions.thoughtkeeper.view.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseActivity
import ru.makproductions.thoughtkeeper.view.note.NoteActivity
import ru.makproductions.thoughtkeeper.view.note.adapter.NotesAdapter
import ru.makproductions.thoughtkeeper.view.splash.SplashActivity
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewModel
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewState

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {
    fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener(
                {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
            )
    }

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    lateinit var adapter: NotesAdapter
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        notes_recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter { NoteActivity.start(this, it.id) }
        notes_recycler_view.adapter = adapter
        add_note_fab.setOnClickListener { viewModel.addNote() }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = MenuInflater(this).inflate(R.menu.options_menu, menu).let { true }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_button_title
            messageResource = R.string.logout_message
            positiveButton("Yes") { onLogout() }
            negativeButton("No") { dialog -> dialog.dismiss() }
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }
}
