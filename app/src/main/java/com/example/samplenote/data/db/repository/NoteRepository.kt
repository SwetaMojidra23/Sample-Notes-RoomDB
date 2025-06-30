package com.example.samplenote.data.db.repository

import androidx.lifecycle.LiveData
import com.example.samplenote.data.db.dao.NoteDao
import com.example.samplenote.data.db.entity.Note

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }
}