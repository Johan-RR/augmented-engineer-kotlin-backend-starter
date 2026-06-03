package com.it.exalt.domain.order

class SimpleAssert<T>(private val actual: T?) {
    fun isNotNull() {
        if (actual == null) throw AssertionError("Expected value to be not null")
    }

    fun isEqualTo(expected: T) {
        if (actual != expected) throw AssertionError("Expected <$expected> but was <$actual>")
    }
}

fun <T> assertThat(actual: T?): SimpleAssert<T> = SimpleAssert(actual)
