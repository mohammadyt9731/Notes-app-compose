package com.example.notes.ui.model

import com.example.notes.data.db.NoteEntity

data class NoteView(
    val id: Int,
    val title: String,
    val notes: String,
    val isTask: Boolean
)

fun NoteEntity.toNoteView() = NoteView(
    id = id,
    title = title,
    notes = notes,
    isTask = isTask
)