package com.example.svrmaps.ui.sign_in

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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider,
    private val userInteractor: UserInteractor
) : BaseViewModel() {


    private var disposable: Disposable? = null

    private val errorMessageRelay = BehaviorRelay.create<SingleEvent<String>>()
    private val loadingRelay = BehaviorRelay.create<Boolean>()
    private val successSignUpRelay = BehaviorRelay.create<UserAccount>()

    val errorMessage: Observable<SingleEvent<String>> = errorMessageRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val successSignUp: Observable<UserAccount> = successSignUpRelay.hide()



}