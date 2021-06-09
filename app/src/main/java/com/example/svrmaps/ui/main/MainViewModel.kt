package com.example.svrmaps.ui.main

import android.view.MenuItem
import androidx.hilt.lifecycle.ViewModelInject
import com.example.svrmaps.ui.base.BaseViewModel


class MainViewModel @ViewModelInject constructor(
): BaseViewModel() {

    var currentSelectedItemId: Int? = null

    fun wasSelected(clickedItem: MenuItem) = currentSelectedItemId == clickedItem.itemId
}