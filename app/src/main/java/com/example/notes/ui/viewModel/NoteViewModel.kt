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
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

    fun insertNote(noteView: NoteView) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.insertNote(noteView)
    }

    fun updateNote(noteView: NoteView) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.updateNote(noteView)
    }

    fun getNote(id: Int): Flow<NoteView?> = noteRepository.getNote(id)
}