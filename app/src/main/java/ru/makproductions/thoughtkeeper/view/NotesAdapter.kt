package ru.makproductions.thoughtkeeper.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.note_adapter_item.view.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.entity.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_adapter_item, parent, false)
        )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) = holder.bind(notes[position])


    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            note_title.text = note.title
            note_text.text = note.text
        }
    }

}