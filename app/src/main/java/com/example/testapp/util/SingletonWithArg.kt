package com.example.testapp.util

open class SingletonWithArg<out T, in Arg>(creator: (Arg) -> T) {
    private var creator: ((Arg) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: Arg): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}