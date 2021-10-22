package com.kongming.android.younglab.notepad.page

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kongming.android.younglab.R
import com.kongming.android.younglab.notepad.viewmodel.*
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip


private const val NOTE_ID = "note_id"

class NoteDetailFragment : Fragment() {
    private var noteId: Long = 0L

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var noteDetailViewModel : NoteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getLong(NOTE_ID)
        }
        noteViewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)
        noteDetailViewModel = ViewModelProvider(this, NoteDetailViewModelFactory(noteId)).get(NoteDetailViewModel::class.java)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
        initListener()
    }

    private fun initListener() {
        btn_submit.setOnClickListener {
            noteDetailViewModel.sendEvent(NoteDetailContract.Event.SaveNoteChanges(edt_detail_title.text.toString(), edt_detail_desc.text.toString()))
        }
    }

    private fun initViews() {

    }

    private fun initObserver() {
        lifecycleScope.launchWhenStarted {
            // 根据多个ViewState更新UI，使用combine操作符
            noteViewModel.uiState.combine(noteDetailViewModel.uiState) { activityState, fragmentState ->
                Pair(activityState.noteList.size, fragmentState.savedTitle)
            }.collect {
                txv_listSize.text = "note列表item:${it.first}个， 此note的Title : ${it.second}".toEditable()
            }
        }



        lifecycleScope.launchWhenStarted {
            noteDetailViewModel.effect.collect {
                when(it) {
                    is NoteDetailContract.Effect.FirstLoadNoteDetail -> {
                        edt_detail_title.text = it.noteItem.title.toEditable()
                        edt_detail_desc.text = it.noteItem.description.toEditable()
                    }
                    is NoteDetailContract.Effect.SaveNoteResult -> {
                        if (it.success) {
                            Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show()
                            noteViewModel.sendEvent(NoteContract.Event.NoteEditCompleted)
                        } else {
                            Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(noteId: Long) =
            NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(NOTE_ID, noteId)
                }
            }
    }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)