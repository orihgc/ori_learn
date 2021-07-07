package com.ori.fragment_learn.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ori.fragment_learn.R

class Fragment1 : Fragment() {
    companion object {
        const val ARG_PARAM = "param_key"

        fun newInstance(str: String): Fragment1 {
            val fragment1 = Fragment1()
            val bundle = Bundle().apply {
                putString(ARG_PARAM, str)
            }
            fragment1.arguments = bundle
            return fragment1
        }
    }

    private var mParam: String? = null
    private var mActivity: Activity? = null

    override fun onAttach(context: Context) {
        Log.e("Fragment1","onAttach Begin")
        super.onAttach(context)
        mActivity = context as Activity
        mParam = arguments?.getString(ARG_PARAM)
        Log.e("Fragment1","onAttach End")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Fragment1","onCreate Begin")
        super.onCreate(savedInstanceState)
        Log.e("Fragment1","onCreate End")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Fragment1","onCreateView Begin")
        val root = inflater.inflate(R.layout.fragment_1, container, false)
        val textView = root.findViewById<TextView>(R.id.text1)
        textView.text = mParam
        Log.e("Fragment1","onCreateView End")
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.e("Fragment1","onActivityCreated Begin")
        super.onActivityCreated(savedInstanceState)
        Log.e("Fragment1","onActivityCreated End")
    }

    override fun onStart() {
        Log.e("Fragment1","onStart Begin")
        super.onStart()
        Log.e("Fragment1","onStart End")
    }

    override fun onResume() {
        Log.e("Fragment1","onResume Begin")
        super.onResume()
        Log.e("Fragment1","onStart End")
    }

    override fun onPause() {
        Log.e("Fragment1","onPause Begin")
        super.onPause()
        Log.e("Fragment1","onPause End")
    }

    override fun onStop() {
        Log.e("Fragment1","onStop Begin")
        super.onStop()
        Log.e("Fragment1","onStop End")
    }

    override fun onDestroy() {
        Log.e("Fragment1","onDestroy Begin")
        super.onDestroy()
        Log.e("Fragment1","onDestroy End")
    }

    override fun onDestroyView() {
        Log.e("Fragment1","onDestroyView Begin")
        super.onDestroyView()
        Log.e("Fragment1","onDestroyView End")
    }

    override fun onDetach() {
        Log.e("Fragment1","onDetach Begin")
        super.onDetach()
        Log.e("Fragment1","onDetach End")
    }


}