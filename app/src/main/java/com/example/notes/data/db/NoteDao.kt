package com.example.notes.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM note WHERE id=:id")
    fun getNote(id: Int): Flow<NoteEntity?>

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note Where title LIKE '%'||:searchedText||'%' OR notes LIKE '%'||:searchedText||'%'")
    fun searchNotes(searchedText: String): Flow<List<NoteEntity>>

}
