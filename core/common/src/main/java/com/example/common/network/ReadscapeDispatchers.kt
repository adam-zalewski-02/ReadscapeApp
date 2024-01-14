package com.example.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val readscapeDispatcher: ReadscapeDispatchers)
enum class ReadscapeDispatchers {
    Default,
    IO,
}