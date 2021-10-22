package com.kongming.android.younglab.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> request(req: () -> T): T {
    return withContext(Dispatchers.IO) {
        req.invoke()
    }
}

