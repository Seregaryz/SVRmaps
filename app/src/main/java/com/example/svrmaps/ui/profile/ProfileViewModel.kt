package com.example.svrmaps.ui.profile

import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.system.SessionKeeper
import com.example.svrmaps.ui.base.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionKeeper: SessionKeeper
): BaseViewModel() {

    private val profileDataRelay = BehaviorRelay.create<String>()

    val profileData: Observable<String> = profileDataRelay.hide()

    init {
        sessionKeeper.userAccount?.let {
            profileDataRelay.accept(it.email)
        }
    }
}