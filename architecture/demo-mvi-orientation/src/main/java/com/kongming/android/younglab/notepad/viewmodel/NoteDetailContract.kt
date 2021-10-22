package com.kongming.android.younglab.notepad.viewmodel

import com.kongming.android.younglab.base.UiEffect
import com.kongming.android.younglab.base.UiEvent
import com.kongming.android.younglab.base.UiState
import com.kongming.android.younglab.notepad.bean.NoteItem

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/15
 */
class NoteDetailContract {

    data class State(
        val loadSuccess: Boolean,
        val savedTitle: String,
        val savedDesc: String
    ) : UiState

    sealed class Event : UiEvent {
        data class SaveNoteChanges(val title: String, val desc: String) : Event()
    }

    sealed class Effect : UiEffect {
        data class FirstLoadNoteDetail(val noteItem: NoteItem) : Effect()
        data class SaveNoteResult(val success: Boolean) : Effect()
    }
}