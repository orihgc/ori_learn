package com.ori.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


/**
 *
 * */

fun channelFlow(){
    Channel<Int>().receiveAsFlow()
}

fun main() {

}