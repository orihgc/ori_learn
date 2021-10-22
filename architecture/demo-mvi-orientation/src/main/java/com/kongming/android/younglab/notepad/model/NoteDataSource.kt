package com.kongming.android.younglab.notepad.model

import com.kongming.android.younglab.notepad.bean.NoteItem
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicLong

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/14
 */
object NoteDataSource {
    const val SUCCESS = 0
    const val QUERY_COST_TIME = 200L

    data class NoteItemResponse(
        val errorCode: Int,
        val hasMore: Boolean,
        val newCursor: Int,
        val noteList: List<NoteItem>
    )

    data class NoteDetailResponse(
        val errCode: Int,
        val noteDetail: NoteItem
    )

    data class AddNoteResponse(
        val errCode: Int
    )

    data class EditNoteResponse(
        val errCode: Int
    )

    private val noteList = mutableListOf<NoteItem>()

    @Volatile
    private var primaryKeyId = AtomicLong(0L)

    suspend fun getNoteList(cursor: Int, limit: Int): NoteItemResponse {
        delay(QUERY_COST_TIME)
        if (limit < 0) return NoteItemResponse(SUCCESS, false, 0, listOf())
        if (cursor >= noteList.size) return NoteItemResponse(
            SUCCESS,
            false,
            noteList.size,
            listOf()
        )
        val from = if (cursor >= 0) cursor else 0
        val to = if (from + limit < noteList.size) from + limit + 1 else noteList.size
        val hasMore = to < noteList.size
        return NoteItemResponse(SUCCESS, hasMore, to + 1, noteList.subList(from, to))
    }

    suspend fun getNoteDetail(id: Long): NoteDetailResponse {
        delay(QUERY_COST_TIME)
        val noteDetail = noteList.firstOrNull {
            it.id == id
        }
        return if (noteDetail == null) {
            NoteDetailResponse(1, NoteItem(id, "", ""))
        } else {
            NoteDetailResponse(SUCCESS, NoteItem(id, noteDetail.title, noteDetail.description))
        }
    }

    suspend fun addNote(title: String, desc: String): AddNoteResponse {
        delay(QUERY_COST_TIME)
        noteList.add(0, NoteItem(primaryKeyId.getAndIncrement(), title, desc))
        return AddNoteResponse(SUCCESS)
    }

    suspend fun completeNoteEdition(id: Long, title: String, desc: String): EditNoteResponse {
        delay(QUERY_COST_TIME)
        noteList.remove(noteList.firstOrNull {
            it.id == id
        })
        noteList.add(0, NoteItem(id, title, desc))
        return EditNoteResponse(SUCCESS)
    }
}