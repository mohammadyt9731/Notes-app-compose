package com.example.notes.data.repository

import com.example.notes.data.dataSource.NoteLocalDataSource
import com.example.notes.data.db.toNoteEntity
import com.example.notes.ui.model.NoteView
import com.example.notes.ui.model.toNoteView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteLocalDataSource: NoteLocalDataSource) {

    suspend fun insertNote(noteView: NoteView) =
        noteLocalDataSource.insertNote(noteView.toNoteEntity())

    suspend fun deleteNote(noteView: NoteView) =
        noteLocalDataSource.deleteNote(noteView.toNoteEntity())

    suspend fun updateNote(noteView: NoteView) =
        noteLocalDataSource.updateNote(noteView.toNoteEntity())

    fun getNote(id: Int): Flow<NoteView?> =
        noteLocalDataSource.getNote(id).map { it?.toNoteView() }

    fun searchNotes(searchedText: String): Flow<List<NoteView>> {
        return if (searchedText.isEmpty()) {
            noteLocalDataSource.getAllNotes().map {
                it.map { noteEntity -> noteEntity.toNoteView() }
            }
        } else {
            noteLocalDataSource.searchNotes(searchedText).map {
                it.map { noteEntity -> noteEntity.toNoteView() }
            }
        }
    }
}