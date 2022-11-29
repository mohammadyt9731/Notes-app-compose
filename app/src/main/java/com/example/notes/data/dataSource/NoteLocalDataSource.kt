package com.example.notes.data.dataSource

import com.example.notes.data.db.NoteDao
import com.example.notes.data.db.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteLocalDataSource @Inject constructor(
    private val noteDao: NoteDao
) {

    suspend fun insertNote(noteEntity: NoteEntity) =
        noteDao.insertNote(noteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity) =
        noteDao.deleteNote(noteEntity)

    suspend fun updateNote(noteEntity: NoteEntity) =
        noteDao.updateNote(noteEntity)

    fun getNote(id: Int): Flow<NoteEntity?> = noteDao.getNote(id)

    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun searchNotes(searchedText: String): Flow<List<NoteEntity>> =
        noteDao.searchNotes(searchedText)

    fun getAllTasks(): Flow<List<NoteEntity>> = noteDao.getAllTasks()

    fun searchTasks(searchedText: String): Flow<List<NoteEntity>> =
        noteDao.searchTasks(searchedText)
}