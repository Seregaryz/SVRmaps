package com.example.svrmaps.ui.profile

import com.example.svrmaps.model.exchange.Exchange
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
) : BaseViewModel() {

    private val profileDataRelay = BehaviorRelay.create<String>()
    private val exchangesDataRelay = BehaviorRelay.create<List<Exchange>>()

    val profileData: Observable<String> = profileDataRelay.hide()
    val exchangesData: Observable<List<Exchange>> = exchangesDataRelay.hide()

    init {
        sessionKeeper.userAccount?.let {
            profileDataRelay.accept(it.email)
        }
        exchangesDataRelay.accept(
            listOf(
                Exchange(
                    offerSubjectName = "Чехол на телефон xiaomi mi5",
                    offerSubjectDescription = "description",
                    offerLatitude = 55.7920723,
                    offerLongitude = 49.1206697,
                    offerUserEmail = "seregaryz@gnail.com",
                    destSubjectName = "Кепка фк \"Спартак\"",
                    destSubjectDescription = "description",
                    destLatitude = 55.789373,
                    destLongitude = 49.12086,
                    destUserEmail = "ivan_vasiliev@gmail.com"
                )
            )
        )
    }
}