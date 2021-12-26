package com.kongming.android.younglab.demo

import com.kongming.android.younglab.base.UiEffect
import com.kongming.android.younglab.base.UiEvent
import com.kongming.android.younglab.base.UiState

class DemoContract {

    data class State(val name: String) : UiState

    sealed class Event : UiEvent {
        data class ChangeText(val name: String) : Event()
    }

    sealed class Effect : UiEffect {}
}