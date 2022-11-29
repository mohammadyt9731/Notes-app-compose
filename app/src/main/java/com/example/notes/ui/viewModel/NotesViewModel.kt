package com.example.notes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.repository.NoteRepository
import com.example.notes.ui.model.NoteView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    fun searchNotes(searchedText: String): Flow<List<NoteView>> =
        noteRepository.searchNotes(searchedText)

    fun deleteNote(noteView: NoteView) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.deleteNote(noteView)
    }
}