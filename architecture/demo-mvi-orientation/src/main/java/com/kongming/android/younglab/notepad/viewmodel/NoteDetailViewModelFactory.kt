package com.kongming.android.younglab.notepad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/15
 */
class NoteDetailViewModelFactory(private val noteId: Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (NoteDetailViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Long::class.java).newInstance(noteId)
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else {
            super.create(modelClass)
        }
    }
}