package com.kongming.android.younglab.demo

import com.kongming.android.younglab.base.BaseViewModel

class MainViewModel : BaseViewModel<DemoContract.State, DemoContract.Event, DemoContract.Effect>() {
    override fun createInitialState(): DemoContract.State = DemoContract.State("ORIHGC")

    override fun handleEvent(event: DemoContract.Event) {
        when(event){
            is DemoContract.Event.ChangeText->{
                setState {
                    copy(name = event.name)
                }
            }
        }
    }
}