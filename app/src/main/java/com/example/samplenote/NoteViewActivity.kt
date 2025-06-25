package com.example.samplenote

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samplenote.data.db.entity.Note
import com.google.gson.Gson


class NoteViewActivity : AppCompatActivity() {

    private val tvNoteTitle: TextView by lazy { findViewById(R.id.tvNoteTitle) }
    private val tvNoteContent: TextView by lazy { findViewById(R.id.tvNoteContent) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContentView(R.layout.activity_note_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gson = Gson()
        val note: Note =
            gson.fromJson<Note>(intent.getStringExtra("Note"), Note::class.java)

//        val noteSerializable: NoteSerializable =
//            intent.getSerializableExtra("Note") as NoteSerializable

        tvNoteTitle.text = note.title
        tvNoteContent.text = note.content

    }
}