package bi.vovota.madeinburundi.utils

import android.util.Log

object Logger {
    private const val  TAG = "MIB"

    fun d(subTag: String, body: String) {
        Log.d(TAG, "[$subTag]: $body")
    }

    fun e(subTag: String, body: String, throwable: Throwable? = null) {
        if (throwable != null) Log.e(TAG, "[$subTag]: $body", throwable) else Log.e(TAG, "[$subTag]: $body")
    }
}