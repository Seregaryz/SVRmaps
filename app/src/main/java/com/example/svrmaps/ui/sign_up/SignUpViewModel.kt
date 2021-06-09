package com.example.svrmaps.ui.sign_up

import androidx.hilt.lifecycle.ViewModelInject
import com.example.predicate.model.schedulers.SchedulersProvider
import com.example.svrmaps.interactor.UserInteractor
import com.example.svrmaps.model.user.UserAccount
import com.example.svrmaps.model.user.UserSignUpItem
import com.example.svrmaps.system.ErrorHandler
import com.example.svrmaps.system.SingleEvent
import com.example.svrmaps.system.acceptSingleEvent
import com.example.svrmaps.ui.base.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignUpViewModel @ViewModelInject constructor(
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    val currentUserData = UserSignUpItem()

    private var disposable: Disposable? = null

    private val errorMessageRelay = BehaviorRelay.create<SingleEvent<String>>()
    private val loadingRelay = BehaviorRelay.create<Boolean>()
    private val successSignUpRelay = BehaviorRelay.create<UserAccount>()

    val errorMessage: Observable<SingleEvent<String>> = errorMessageRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val successSignUp: Observable<UserAccount> = successSignUpRelay.hide()

    fun createAccount() {
        disposable = userInteractor.createAccount(currentUserData.email, currentUserData.password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        currentUserData.email != "" && currentUserData.password != "" && currentUserData.confirmPassword != ""
}