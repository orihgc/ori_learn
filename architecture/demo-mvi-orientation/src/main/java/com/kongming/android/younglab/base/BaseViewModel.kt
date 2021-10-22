package com.kongming.android.younglab.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/12
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {

    /**
     * 初始状态
     * stateFlow区别于LiveData必须有初始值
     */
    private val initialState: State by lazy { createInitialState() }

    abstract fun createInitialState(): State

    /**
     * uiState聚合页面的全部UI
     * 使用stateFlow，未来兼容compose
     */
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    /**
     * event即用户对ui的操作流，如点击操作
     * 使用sharedFlow原因在于：
     * 1. replay默认为0  如果当前无subscriber 直接drop 更符合event的特性
     */
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    /**
     * effect用作一次性事件，一般是一对一的订阅关系
     * 例如：弹Toast、导航Fragment等
     * 使用channel用于代替SingleLiveEvent
     */
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    val currentState: State
        get() = uiState.value

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    protected abstract fun handleEvent(event: Event)

    fun sendEvent(event: Event) {
        val newEvent = event
        viewModelScope.launch {
            _event.emit(newEvent)
        }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val newEffect = builder()
        viewModelScope.launch {
            _effect.send(newEffect)
        }
    }
}