package com.example.testapp.util

open class Singleton<out T>(creator: () -> T) {
    private var creator: (() -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = requireNotNull(creator?.invoke())
                instance = created
                creator = null
                created
            }
        }
    }
}