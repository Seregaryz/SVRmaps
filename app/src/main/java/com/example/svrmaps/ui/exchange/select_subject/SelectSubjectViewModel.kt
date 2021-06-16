package com.example.svrmaps.ui.exchange.select_subject

import com.example.predicate.model.schedulers.SchedulersProvider
import com.example.svrmaps.interactor.SubjectInteractor
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.system.ErrorHandler
import com.example.svrmaps.system.SessionKeeper
import com.example.svrmaps.system.SingleEvent
import com.example.svrmaps.ui.base.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class SelectSubjectViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider,
    private val interactor: SubjectInteractor,
    private val sessionKeeper: SessionKeeper
): BaseViewModel() {

    private var disposable: Disposable? = null

    private val errorMessageRelay = BehaviorRelay.create<SingleEvent<String>>()
    private val loadingRelay = BehaviorRelay.create<Boolean>()
    private val subjectsDataRelay = BehaviorRelay.create<List<Subject>>()

    val errorMessage: Observable<SingleEvent<String>> = errorMessageRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val subjectsData: Observable<List<Subject>> = subjectsDataRelay.hide()

    init {
        getMySubjects()
    }

    fun getMySubjects() {
        subjectsDataRelay.accept(
            listOf(
                Subject(
                    "Чехол на телефон xiaomi mi5",
                    "Черный чехол на телефон, мне не подошел",
                    55.7923736,
                    49.1210780,
                    sessionKeeper.userAccount?.email
                )
            )
        )
//        disposable = interactor.getSubjects()
//            .subscribeOn(schedulers.io())
//            .observeOn(schedulers.ui())
//            .doOnSubscribe { loadingRelay.accept(true) }
//            .doFinally { loadingRelay.accept(false) }
//            .subscribe (
//                {
//                    subjectsDataRelay.accept(
//                        listOf(
//                            Subject(
//                                "Ручка",
//                                "Хорошая ручка",
//                                55.7920736,
//                                49.1206780
//                            )
//                        )
//                    )
//                }, { e ->
//                    errorHandler.proceed(e) {
//                        errorMessageRelay.acceptSingleEvent(it)
//                    }
//                }
//            )

    }
}