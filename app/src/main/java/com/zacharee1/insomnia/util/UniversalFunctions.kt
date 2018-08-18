package com.zacharee1.insomnia.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import com.google.gson.Gson
import com.zacharee1.insomnia.tiles.CycleTile

const val TAG = "Insomnia"

const val KEY_USE_INFINITE = "use_infinite"
const val KEY_STATES = "states"

fun String.loge() {
    Log.e(TAG, this)
}

fun String.logv() {
    Log.v(TAG, this)
}

fun String.logi() {
    Log.i(TAG, this)
}

fun String.logd() {
    Log.d(TAG, this)
}

fun String.logw() {
    Log.w(TAG, this)
}

fun String.loga() {
    Log.println(Log.ASSERT, TAG, this)
}

fun Context.getSavedTimes(): ArrayList<CycleTile.WakeState> {
    val gson = Gson()
    val strings = PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_STATES, null) ?: return CycleTile.DEFAULT_STATES
    val ret = ArrayList<CycleTile.WakeState>()

    strings.split(CycleTile.DELIMITER).forEach {
        val state = gson.fromJson<CycleTile.WakeState>(it, CycleTile.WakeState::class.java)
        if (state != null) ret.add(state)
    }

    return ret
}

fun Context.saveTimes(times: ArrayList<CycleTile.WakeState>) {
    val gson = Gson()
    val strings = ArrayList<String>()

    times.forEach {
        strings.add(gson.toJson(it))
    }

    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_STATES, TextUtils.join(CycleTile.DELIMITER, strings)).apply()
}

fun Context.useInfinite()
        = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_USE_INFINITE, true)

fun Context.setUseInfinite(useInfinite: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(KEY_USE_INFINITE, useInfinite).apply()
}

/**
 * Convert a certain DP value to its equivalent in px
 * @param dpVal the chosen DP value
 * @return the DP value in terms of px
 */
fun Context.dpAsPx(dpVal: Int) =
        dpAsPx(dpVal.toFloat())

fun Context.dpAsPx(dpVal: Float) =
        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics))

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)

    return bitmap
}