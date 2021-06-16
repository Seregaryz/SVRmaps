package com.example.svrmaps.network.repository

import com.example.svrmaps.model.subject.Subject
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

interface SubjectRepository {

    fun getSubjectsFromFb(): Observable<List<Subject>>

    fun createSubject(
        name: String, description: String, latitude: Double?, longitude: Double?, email: String?
    ): Single<String>

}