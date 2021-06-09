package com.example.svrmaps.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.svrmaps.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

fun Fragment.hideKeyboard() {
    activity?.apply {
        currentFocus?.apply {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun Fragment.showSoftInput(view: View) {
    activity?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.snackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.let { Snackbar.make(it, message, duration).show() }
}

fun Fragment.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message?.let { Toast.makeText(context, message, duration).show() }
}

inline fun Fragment.snackbarWithAction(
    message: String,
    duration: Int = Snackbar.LENGTH_INDEFINITE,
    crossinline action: () -> Unit
) {
    view?.let {
        val snackbar = Snackbar.make(it, message, duration)
        snackbar.setAction("Повторить") { action() }
        snackbar.show()
    }
}

fun Fragment.addHardwareNavigationPadding(view: View) {
    if (activity?.window?.decorView?.isAttachedToWindow == true) {
        activity?.window?.decorView?.addSystemBottomPadding(view)
    }
}

fun BottomSheetDialog.tweakFitSystemWindows() {
    window?.findViewById<View>(R.id.container)?.fitsSystemWindows = false
    window?.findViewById<View>(R.id.coordinator)?.fitsSystemWindows = false
}
