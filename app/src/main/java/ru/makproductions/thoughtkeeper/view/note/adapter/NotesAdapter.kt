package ru.makproductions.thoughtkeeper.view.note.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.note_adapter_item.view.*
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.common.toResource
import ru.makproductions.thoughtkeeper.model.entity.Note

class NotesAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var notes: List<Note>? = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_adapter_item, parent, false)
        )

    override fun getItemCount() = if (notes == null) 0 else notes!!.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) = holder.bind(notes?.get(position))


    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note?) = with(itemView) {
            note?.let {
                note_title.text = note.title
                note_text.text = note.text
                setOnClickListener({ onItemClick?.invoke(note) })
                setBackgroundColor(ContextCompat.getColor(itemView.context, note.color.toResource()))
            }.let { }

        }
    }

}