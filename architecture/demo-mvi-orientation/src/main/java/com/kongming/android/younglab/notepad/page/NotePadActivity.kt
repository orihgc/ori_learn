package com.kongming.android.younglab.notepad.page

import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kongming.android.younglab.R
import com.kongming.android.younglab.base.BaseActivity
import com.kongming.android.younglab.notepad.bean.NoteItem
import com.kongming.android.younglab.notepad.view.CustomLoadMoreView
import com.kongming.android.younglab.notepad.view.HDCustomDialog
import com.kongming.android.younglab.notepad.viewmodel.NoteContract
import com.kongming.android.younglab.notepad.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.notepad_activity.*
import kotlinx.coroutines.flow.collect

class NotePadActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.notepad_activity


    private val viewModel: NoteViewModel by viewModels()

    private val adapter = NoteListAdapter()

    override fun initData() {
        super.initData()
    }

    override fun initViews() {
        super.initViews()
        initAdapter()
        initRecycleView()
        initListener()
    }

    private fun initListener() {
        btn_floating.setOnClickListener {
            viewModel.sendEvent(NoteContract.Event.AddingButtonClickEvent)
        }
    }

    private fun initRecycleView() {
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setOnRefreshListener {
            viewModel.refreshNotes()
        }
        rcv_room_list.adapter = adapter
        rcv_room_list.layoutManager = LinearLayoutManager(this)
        rcv_room_list.hasFixedSize()
    }

    private fun initAdapter() {
        adapter.loadMoreModule.apply {
            loadMoreView = CustomLoadMoreView()
            isAutoLoadMore = true
            setOnLoadMoreListener {
                viewModel.loadMoreNotes()
            }
        }

        adapter.setOnItemClickListener { _, _, position ->
            viewModel.sendEvent(NoteContract.Event.ListItemClickEvent(adapter.getItem(position)))
        }

        adapter.setDiffCallback(object : DiffUtil.ItemCallback<NoteItem>() {
            override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem == newItem
            }
        })
    }

    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it.loadStatus) {
                    is NoteContract.LoadStatus.LoadMoreLoading -> {
                        adapter.loadMoreModule.loadMoreToLoading()
                    }
                    is NoteContract.LoadStatus.LoadMoreError -> {
                        adapter.loadMoreModule.loadMoreFail()
                    }
                    is NoteContract.LoadStatus.LoadMoreFailed -> {
                        adapter.loadMoreModule.loadMoreFail()
                    }
                    is NoteContract.LoadStatus.LoadMoreSuccess -> {
                        adapter.setDiffNewData(it.noteList)
                        if (it.loadStatus.hasMore) {
                            adapter.loadMoreModule.loadMoreComplete()
                        } else {
                            adapter.loadMoreModule.loadMoreEnd(false)
                        }
                    }
                    else -> {
                        adapter.setDiffNewData(it.noteList)
                    }
                }

                when (it.refreshStatus) {
                    is NoteContract.RefreshStatus.RefreshSuccess -> {
                        adapter.setDiffNewData(it.noteList)
                        refresh_layout.finishRefresh()
                        if (it.refreshStatus.hasMore) {
                            adapter.loadMoreModule.loadMoreComplete()
                        } else {
                            adapter.loadMoreModule.loadMoreEnd(false)
                        }
                    }
                    is NoteContract.RefreshStatus.RefreshFailed -> {
                        refresh_layout.finishRefresh(false)
                    }
                    is NoteContract.RefreshStatus.RefreshError -> {
                        refresh_layout.finishRefresh(false)
                    }
                    else -> {
                        adapter.setDiffNewData(it.noteList)
                    }
                }
                txv_title.text = it.pageTitle
                txv_desc.text = "${it.noteList.size}条记录"
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    is NoteContract.Effect.ShowErrorToastEffect -> {
                        showToast(it.text)
                    }
                    is NoteContract.Effect.JumpToNoteFragment -> {
                        guideToNoteDetailPage(it.id)
                    }
                    is NoteContract.Effect.ShowAddNoteDialog -> {
                        showAddNoteDialog()
                    }
                    is NoteContract.Effect.AddNote -> {
                        showToast("try log add note")
                        viewModel.addNote(it.title, it.desc)
                    }
                }
            }
        }
    }

    private fun showAddNoteDialog() {
        HDCustomDialog {
            this.layoutId = R.layout.dialog_add_note
            this.cancelable = true
            this.canceledOnTouchOutside = true

            dismissCallback = {
                viewModel.sendEvent(NoteContract.Event.AddingNoteDialogDismiss)
            }
            this.onViewInflater = { dialog ->
                dialog.listeners({
                    val titleText = dialog.getView<EditText>(R.id.edt_title).text.toString()
                    val descText = dialog.getView<EditText>(R.id.edt_desc).text.toString()
                    viewModel.sendEvent(
                        NoteContract.Event.AddingNoteDialogConfirm(
                            titleText,
                            descText
                        )
                    )
                    dialog.dismiss()
                }, R.id.btn_add)

                dialog.listeners({
                    viewModel.sendEvent(NoteContract.Event.AddingNoteDialogCanceled)
                    dialog.dismiss()
                }, R.id.btn_cancel)
            }
        }.show(this)
    }

    private fun guideToNoteDetailPage(noteId: Long) {
        val fragmentExisted = supportFragmentManager.findFragmentByTag(NOTE_DETAIL_NAME) != null
        supportFragmentManager.beginTransaction().apply {
            if (fragmentExisted) {
                supportFragmentManager.popBackStack()
            }
            replace(R.id.fl_detail, NoteDetailFragment.newInstance(noteId), NOTE_DETAIL_NAME)
            addToBackStack(NOTE_DETAIL_NAME)
            commitAllowingStateLoss()
        }
    }

    companion object {
        const val NOTE_DETAIL_NAME = "note_detail_fragment"
    }
}