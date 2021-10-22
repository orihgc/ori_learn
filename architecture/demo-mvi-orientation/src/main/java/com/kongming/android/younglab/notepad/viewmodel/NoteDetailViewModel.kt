package com.kongming.android.younglab.notepad.viewmodel

import androidx.lifecycle.viewModelScope
import com.kongming.android.younglab.base.BaseViewModel
import com.kongming.android.younglab.notepad.model.NoteDataSource.SUCCESS
import com.kongming.android.younglab.notepad.model.NoteDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/15
 */
class NoteDetailViewModel(private val noteId: Long) :
    BaseViewModel<NoteDetailContract.State, NoteDetailContract.Event, NoteDetailContract.Effect>() {


    private val detailRepository = NoteDetailRepository()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            detailRepository.fetchDetail(noteId).collect {
                if (it.errCode == SUCCESS) {
                    setState {
                        NoteDetailContract.State(true, it.noteDetail.title, it.noteDetail.description)
                    }
                    setEffect {
                        NoteDetailContract.Effect.FirstLoadNoteDetail(it.noteDetail)
                    }
                } else {
                    setState {
                        NoteDetailContract.State(false,"", "")
                    }
                }
            }
        }

    }

    override fun createInitialState(): NoteDetailContract.State = NoteDetailContract.State(false, "", "")


    override fun handleEvent(event: NoteDetailContract.Event) {
        when (event) {
            is NoteDetailContract.Event.SaveNoteChanges -> {
                saveNote(event.title, event.desc)
            }
        }
    }

    private fun saveNote(title: String, desc: String) {
        viewModelScope.launch(Dispatchers.Main) {
            detailRepository.saveNote(noteId, title, desc).collect {
                if (it.errCode == SUCCESS) {
                    setEffect {
                        NoteDetailContract.Effect.SaveNoteResult(it.errCode == SUCCESS)
                    }
                    setState {
                        NoteDetailContract.State(true, title, desc)
                    }
                }
            }
        }
    }


}