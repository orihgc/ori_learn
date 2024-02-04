package com.ori.recycler_view_learn.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.ori.recycler_view_learn.R
import kotlinx.android.synthetic.main.plugin_rtc_widget_resolution_selection.view.rv_solution_selection

class ResolutionSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    var resolutionList: MutableList<String> = mutableListOf()
        set(value) {
            val resolutionAdapter = ResolutionAdapter(value)
            rv_solution_selection.layoutManager = LinearLayoutManager(context)
            rv_solution_selection.adapter = resolutionAdapter
            resolutionAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {

            })

        }


    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context)
            .inflate(R.layout.plugin_rtc_widget_resolution_selection, this, true)
    }

}