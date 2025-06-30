package com.example.samplenote.data.db.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.samplenote.data.db.NoteDatabase
import com.example.samplenote.data.db.entity.Note
import com.example.samplenote.data.db.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.getAllNotes()
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
             repository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }


}