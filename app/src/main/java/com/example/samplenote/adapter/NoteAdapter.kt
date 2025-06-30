package com.example.samplenote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenote.R
import com.example.samplenote.click.NoteDiffCallback
import com.example.samplenote.data.db.entity.Note

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit,
    private val onNoteDeleteClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
//        var inVisibleLayout: LinearLayout = itemView.findViewById(R.id.inVisibleLayout)
        var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)


        fun bind(note: Note, isLast: Boolean) {
            tvTitle.text = note.title
//            inVisibleLayout.visibility = if (isLast) View.VISIBLE else View.GONE

            // Always explicitly reset visibility to avoid reuse issues
            itemView.setOnClickListener { onNoteClick(note) }

            itemView.setOnLongClickListener {
                onNoteLongClick(note)
                true
            }


            imgDelete.setOnClickListener { onNoteDeleteClick(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, position == currentList.lastIndex)
    }
}
