package com.kongming.android.younglab.notepad.viewmodel

import com.kongming.android.younglab.base.UiEffect
import com.kongming.android.younglab.base.UiEvent
import com.kongming.android.younglab.base.UiState
import com.kongming.android.younglab.notepad.bean.NoteItem

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/12
 */
class NoteContract {

    data class State(
        val pageTitle: String,
        val loadStatus: LoadStatus,
        val refreshStatus: RefreshStatus,
        val noteList: MutableList<NoteItem>
    ) : UiState

    sealed class Event : UiEvent {
        object AddingButtonClickEvent : Event()
        data class ListItemClickEvent(val item: NoteItem) : Event()
        object AddingNoteDialogDismiss : Event()
        data class AddingNoteDialogConfirm(val title: String, val desc: String) : Event()
        object AddingNoteDialogCanceled : Event()
        object NoteEditCompleted : Event()
    }

    sealed class Effect : UiEffect {
        data class ShowErrorToastEffect(val text: String) : Effect()
        object ShowAddNoteDialog : Effect()
        data class JumpToNoteFragment(val id: Long) : Effect()
        data class AddNote(val title: String, val desc: String) :Effect()
    }

    sealed class LoadStatus {
        object LoadMoreInit : LoadStatus()
        object LoadMoreLoading : LoadStatus()
        data class LoadMoreSuccess(val hasMore: Boolean) : LoadStatus()
        data class LoadMoreError(val exception: Throwable) : LoadStatus()
        data class LoadMoreFailed(val errCode: Int) : LoadStatus()
    }

    sealed class RefreshStatus {
        object RefreshInit : RefreshStatus()
        object RefreshLoading : RefreshStatus()
        data class RefreshSuccess(val hasMore: Boolean) : RefreshStatus()
        data class RefreshError(val exception: Throwable) : RefreshStatus()
        data class RefreshFailed(val errCode: Int) : RefreshStatus()
    }
}