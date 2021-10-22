package com.kongming.android.younglab.notepad.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/13
 */
class NoteRepository {

    private val dataSource = NoteDataSource

    fun fetchNotes(cursor: Int, limit: Int): Flow<NoteDataSource.NoteItemResponse> {
        return flow {
            emit(dataSource.getNoteList(cursor, limit))
        }.flowOn(Dispatchers.IO)
    }

    fun addNote(title: String, desc: String) : Flow<NoteDataSource.AddNoteResponse> {
        return flow {
            emit(dataSource.addNote(title, desc))
        }.flowOn(Dispatchers.IO)
    }

}