package com.example.svrmaps.ui.add_subject

import com.example.predicate.model.schedulers.SchedulersProvider
import com.example.svrmaps.interactor.SubjectInteractor
import com.example.svrmaps.model.user.UserAccount
import com.example.svrmaps.system.ErrorHandler
import com.example.svrmaps.system.SingleEvent
import com.example.svrmaps.system.acceptSingleEvent
import com.example.svrmaps.ui.base.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider,
    private val interactor: SubjectInteractor
) : BaseViewModel() {

    var currentName = ""
    var currentDescription = ""

    private var disposable: Disposable? = null

    private val errorMessageRelay = BehaviorRelay.create<SingleEvent<String>>()
    private val loadingRelay = BehaviorRelay.create<Boolean>()
    private val successSignUpRelay = BehaviorRelay.create<String>()

    val errorMessage: Observable<SingleEvent<String>> = errorMessageRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val successSignUp: Observable<String> = successSignUpRelay.hide()

    fun createSubject(latitude: Double?, longitude: Double?) {
        disposable = interactor.createSubject(currentName, currentDescription, latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doFinally { loadingRelay.accept(false) }
            .subscribe (
                {
                    successSignUpRelay.accept(it)
                }, { e ->
                    errorHandler.proceed(e) {
                        errorMessageRelay.acceptSingleEvent(it)
                    }
                }
            )

    }

    fun validateData(): Boolean =
        currentName != "" && currentDescription != ""
}