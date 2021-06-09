package com.example.svrmaps.utils

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.tabs.TabLayout

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun View.addSystemTopPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        targetView.updatePadding(
            top = initialPadding.top + insets.systemWindowInsetTop
        )
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setSystemWindowInsets(
                Insets.of(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.addSystemTopPadding(
    targetViews: List<View>,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        targetViews.forEach {
            it.updatePadding(
                top = initialPadding.top + insets.systemWindowInsetTop
            )
        }
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setSystemWindowInsets(
                Insets.of(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.addSystemBottomPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        targetView.updatePadding(
            bottom = initialPadding.bottom + insets.systemWindowInsetBottom
        )
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setSystemWindowInsets(
                Insets.of(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    0
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun TabLayout.addOnTabSelectedListener(listener: (position: Int) -> Unit) {
    addOnTabSelectedListener(
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                listener(tab.position)
            }
        }
    )
}


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}


/**
 * Устанавливает лисенер, который возвращает информацию только о скролле по оси Y
 * @param action Лямбда, которая принимает в себя параметром скролл по оси Y
 * и вызывается при скролле
 */
inline fun NestedScrollView.onScrollYListener(crossinline action: (action: Int) -> Unit) {
    return setOnScrollChangeListener(
        NestedScrollView
            .OnScrollChangeListener { _, _, scrollY, _, _ ->
                action(scrollY)
            }
    )
}

fun AppBarLayout.disableScrolling() {
    val params = layoutParams as CoordinatorLayout.LayoutParams
    params.behavior = AppBarLayout.Behavior()
    (params.behavior as? AppBarLayout.Behavior)?.setDragCallback(object : DragCallback() {
        override fun canDrag(appBarLayout: AppBarLayout): Boolean {
            return false
        }
    })
}

