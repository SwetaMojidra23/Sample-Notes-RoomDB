package com.example.samplenote

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenote.data.db.NoteDatabase
import com.example.samplenote.data.db.entity.Note
import com.example.samplenote.data.db.viewModel.NoteViewModel
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val rvRecyclerNotes: RecyclerView by lazy { findViewById(R.id.rvRecyclerNotes) }
    private val imgInsert: ImageView by lazy { findViewById(R.id.imgInsert) }
    private lateinit var noteDatabase: NoteDatabase
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteDatabase = NoteDatabase.getDatabase(this)

        imgInsert.setOnClickListener {
            showNoteDialog(this@MainActivity, "Add", null)
        }

        val allNotes = noteDatabase.noteDao().getAllNotes()

        allNotes.observe(this, { notes ->
            // Update UI with the list of notes
            noteAdapter = NoteAdapter(this, notes)
            rvRecyclerNotes.adapter = noteAdapter
        })

    }

    private class NoteAdapter(private var context: Context, private var noteList: List<Note>) :
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
        class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            var inVisibleLayout: LinearLayout = itemView.findViewById(R.id.inVisibleLayout)
            var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            return NoteViewHolder(
                LayoutInflater.from(context).inflate(R.layout.note_items, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return noteList.size
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            holder.tvTitle.text = noteList[position].title

            holder.itemView.setOnLongClickListener {
                (context as MainActivity).showNoteDialog(
                    context,
                    "Update",
                    noteList[position]
                );
                false
            }

            holder.itemView.setOnClickListener {
                val gson = Gson()
                val myJson = gson.toJson(noteList[position])

                val intent = Intent(context, NoteViewActivity::class.java)
                intent.putExtra("Note", myJson)
                context.startActivity(intent)
            }

            if (position == noteList.size - 1) {
                holder.inVisibleLayout.visibility = View.VISIBLE
            } else {
                holder.inVisibleLayout.visibility = View.GONE
            }

            holder.imgDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { dialog, which ->
                        // Perform delete action here
                        (context as MainActivity).noteViewModel.deleteNote(noteList[position])
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    fun showNoteDialog(context: Context, txtAddOrUpdate: String, note: Note?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(LayoutInflater.from(context).inflate(R.layout.add_note_dialog, null))

        val titleEditText = dialog.findViewById<EditText>(R.id.etNoteTitle)
        val contentEditText = dialog.findViewById<EditText>(R.id.etNoteContent)
        val btnAddNote = dialog.findViewById<Button>(R.id.btnAdNote)
        val btnCancelNote = dialog.findViewById<Button>(R.id.btnCancel)

        if (note != null) {
            titleEditText.setText(note.title)
            contentEditText.setText(note.content)
        }

        btnAddNote.text = txtAddOrUpdate

        btnAddNote.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (note != null && note.id > 0) {
                note.title = title
                note.content = content
                noteViewModel.updateNote(note)
                dialog.dismiss()
            } else {
                val newNote = Note(title = title, content = content)
                noteViewModel.insertNote(newNote)
                dialog.dismiss()
            }
        }

        btnCancelNote.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}