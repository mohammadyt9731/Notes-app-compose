package com.example.notes.data.di

import android.content.Context
import androidx.room.Room
import com.example.notes.data.db.AppDataBase
import com.example.notes.data.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDateBase(@ApplicationContext context: Context): AppDataBase = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        "note_db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideNoteDao(appDataBase: AppDataBase): NoteDao = appDataBase.noteDao()

}