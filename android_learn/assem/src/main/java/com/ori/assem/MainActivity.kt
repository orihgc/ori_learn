package com.ori.assem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.assem.arch.extensions.assemble
import com.ori.assem.base.TaskContentAssem
import com.ori.assem.databinding.MainActivityBinding

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assemble {

//            uiContentAssem {
//                type = TaskAddFloatingAssem::class
//                contentViewId = R.id.container
//            }
            uiSlotAssem {
                type = TaskContentAssem::class
                slotLayoutId = R.id.container
            }
        }
    }
}