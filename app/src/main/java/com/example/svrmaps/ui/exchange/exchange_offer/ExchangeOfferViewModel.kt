package com.example.svrmaps.ui.exchange.exchange_offer

import com.example.predicate.model.schedulers.SchedulersProvider
import com.example.svrmaps.interactor.ExchangeInteractor
import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.system.ErrorHandler
import com.example.svrmaps.system.SingleEvent
import com.example.svrmaps.system.acceptSingleEvent
import com.example.svrmaps.ui.base.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class ExchangeOfferViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider,
    private val intetactor: ExchangeInteractor
) : BaseViewModel() {

    var currentOfferSubject: Subject? = null

    private var disposable: Disposable? = null

    private val errorMessageRelay = BehaviorRelay.create<SingleEvent<String>>()
    private val loadingRelay = BehaviorRelay.create<Boolean>()
    private val successCreatingRelay = BehaviorRelay.create<String>()

    val errorMessage: Observable<SingleEvent<String>> = errorMessageRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val successCreating: Observable<String> = successCreatingRelay.hide()

    fun createExchangeOffer(subject: Subject?) {
        disposable = intetactor.createExchangeOffer(
            Exchange(
              offerSubjectName = currentOfferSubject?.name ?: "",
              offerSubjectDescription = currentOfferSubject?.description ?: "",
              offerLatitude = currentOfferSubject?.latitude,
              offerLongitude = currentOfferSubject?.longitude,
              offerUserEmail = currentOfferSubject?.creatorEmail,
              destSubjectName = subject?.name ?: "",
              destSubjectDescription = subject?.description ?: "",
              destLatitude = subject?.latitude,
              destLongitude = subject?.longitude,
              destUserEmail = subject?.creatorEmail
            )
        )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doFinally { loadingRelay.accept(false) }
            .subscribe (
                {
                    successCreatingRelay.accept(it)
                }, { e ->
                    errorHandler.proceed(e) {
                        errorMessageRelay.acceptSingleEvent(it)
                    }
                }
            )
    }
}