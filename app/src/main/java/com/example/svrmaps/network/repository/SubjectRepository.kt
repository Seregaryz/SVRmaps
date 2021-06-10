package com.example.svrmaps.network.repository

import com.example.svrmaps.model.subject.Subject
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val database: FirebaseDatabase
) {

    private var myRef = database.getReference("subject")
    private var listRef = database.getReference("subject").child("list")

    fun getSubjectsFromFb(): Observable<List<Subject>> {
        val query = myRef
        val indicator = object :
            GenericTypeIndicator<Map<String, @kotlin.jvm.JvmSuppressWildcards Subject>>() {}
        return RxFirebaseDatabase.observeSingleValueEvent(query)
            .toObservable()
            .flatMap { list ->
                Observable.fromIterable(list.getValue(indicator)?.values)
                    .toList()
                    .toObservable()
            }
    }

    fun createSubject(
        name: String, description: String
    ): Single<String> {
        val key = listRef.push().key ?: "null"
        myRef.child(key).setValue(Subject(id = 0, name = name, description = description))
        return Single.just(key)
    }
}