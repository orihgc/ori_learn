package com.ori.assem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ori.assem.databinding.MainFragmentBinding
import com.ori.assem.reused.OriAdapter
import com.ori.assem.reused.OriItem

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
        val dataSource = mutableListOf<OriItem>()
        for (i in 0..20){
            dataSource.add(OriItem(false, i))
        }
        binding.rvContainer.apply {
            adapter = OriAdapter(dataSource, this@MainFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}