package com.kongming.android.younglab.notepad.viewmodel

import androidx.lifecycle.viewModelScope
import com.kongming.android.younglab.base.BaseViewModel
import com.kongming.android.younglab.notepad.model.NoteDataSource.SUCCESS
import com.kongming.android.younglab.notepad.model.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NoteViewModel : BaseViewModel<NoteContract.State, NoteContract.Event, NoteContract.Effect>() {

    private val repository: NoteRepository by lazy {
        NoteRepository()
    }

    private var cursor = 0

    override fun createInitialState() = NoteContract.State(
        "Dali NotePad",
        NoteContract.LoadStatus.LoadMoreInit,
        NoteContract.RefreshStatus.RefreshInit,
        mutableListOf()
    )

    override fun handleEvent(event: NoteContract.Event) {
        when (event) {
            NoteContract.Event.AddingButtonClickEvent -> {
                setEffect {
                    NoteContract.Effect.ShowAddNoteDialog
                }
            }

            is NoteContract.Event.ListItemClickEvent -> {
                setEffect {
                    NoteContract.Effect.JumpToNoteFragment(event.item.id)
                }
            }

            NoteContract.Event.AddingNoteDialogCanceled -> {

            }

            is NoteContract.Event.AddingNoteDialogConfirm -> {
                setEffect {
                    NoteContract.Effect.AddNote(event.title, event.desc)
                }
            }

            NoteContract.Event.AddingNoteDialogDismiss -> {

            }

            NoteContract.Event.NoteEditCompleted -> {
                refreshNotes()
            }
        }
    }


    fun refreshNotes(count: Int = 20) {
        viewModelScope.launch(Dispatchers.Main) {
            cursor = 0
            repository.fetchNotes(cursor, count)
                .onStart {
                    setState {
                        copy(refreshStatus = NoteContract.RefreshStatus.RefreshLoading)
                    }
                }.catch { e ->
                    setState { copy(refreshStatus = NoteContract.RefreshStatus.RefreshError(e)) }
                    setEffect {
                        NoteContract.Effect.ShowErrorToastEffect("刷新数据请求异常,exception: $e")
                    }
                }.onCompletion {
                    setState { copy(refreshStatus = NoteContract.RefreshStatus.RefreshInit) }
                }.collect {
                    when (it.errorCode) {
                        SUCCESS -> {
                            setState {
                                copy(
                                    refreshStatus = NoteContract.RefreshStatus.RefreshSuccess(it.hasMore),
                                    noteList = it.noteList.toMutableList()
                                )
                            }
                            cursor = it.newCursor
                        }
                        else -> {
                            setState {
                                copy(refreshStatus = NoteContract.RefreshStatus.RefreshFailed(it.errorCode))
                            }
                            setEffect {
                                NoteContract.Effect.ShowErrorToastEffect("刷新数据请求错误,errorcode: ${it.errorCode}")
                            }
                        }
                    }
                }
        }
    }

    fun loadMoreNotes(limit: Int = 20) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.fetchNotes(cursor, limit)
                .onStart {
                    setState { copy(loadStatus = NoteContract.LoadStatus.LoadMoreLoading) }
                }
                .catch { e ->
                    setState { copy(loadStatus = NoteContract.LoadStatus.LoadMoreError(e)) }
                    setEffect {
                        NoteContract.Effect.ShowErrorToastEffect("加载数据请求异常,exception: $e")
                    }
                }.onCompletion {
                    setState { copy(loadStatus = NoteContract.LoadStatus.LoadMoreInit) }
                }.collect {
                    when (it.errorCode) {
                        SUCCESS -> {
                            setState {
                                noteList.addAll(it.noteList)
                                copy(
                                    loadStatus = NoteContract.LoadStatus.LoadMoreSuccess(it.hasMore),
                                )
                            }
                            cursor = it.newCursor
                        }
                        else -> {
                            setState { copy(loadStatus = NoteContract.LoadStatus.LoadMoreFailed(it.errorCode)) }
                            setEffect {
                                NoteContract.Effect.ShowErrorToastEffect("加载数据请求错误,errorcode: ${it.errorCode}")
                            }
                        }
                    }
                }
        }
    }

    fun addNote(title: String, desc: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.addNote(title, desc)
                .collect {
                    if (it.errCode == SUCCESS) {
                        refreshNotes()
                    } else {
                        setEffect {
                            NoteContract.Effect.ShowErrorToastEffect("添加失败")
                        }
                    }
                }
        }

    }


}