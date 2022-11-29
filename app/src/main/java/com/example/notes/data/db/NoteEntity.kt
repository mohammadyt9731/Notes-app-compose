package com.example.notes.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notes.ui.model.NoteView

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val notes: String,
    val isTask: Boolean
)

fun NoteView.toNoteEntity() = NoteEntity(
    id = id,
    title = title,
    notes = notes,
    isTask = isTask
)
