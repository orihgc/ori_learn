package com.kongming.android.younglab.notepad.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/15
 */
class NoteDetailRepository {
    private val dataSource = NoteDataSource

    fun fetchDetail(id: Long): Flow<NoteDataSource.NoteDetailResponse> {
        return flow {
            emit(dataSource.getNoteDetail(id))
        }.flowOn(Dispatchers.IO)
    }

    fun saveNote(id: Long, title: String, desc: String) : Flow<NoteDataSource.EditNoteResponse> {
        return flow {
            emit(dataSource.completeNoteEdition(id, title, desc))
        }.flowOn(Dispatchers.IO)
    }
}