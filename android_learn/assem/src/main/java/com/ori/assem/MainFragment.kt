package com.ori.assem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ori.assem.databinding.MainFragmentBinding
import com.ori.assem.powerlist.CommentItem
import com.ori.assem.powerlist.assem.CommentCell

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class MainFragment : Fragment() {


    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.powerList.registerCells(CommentCell::class.java)
        for (i in 0..100) {
            binding.powerList.state.add(CommentItem(i, false, "$i"))
        }
    }
}