package com.example.svrmaps.interactor

import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.network.repository.SubjectRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SubjectInteractor @Inject constructor(
    private val repository: SubjectRepository
) {

    fun createSubject(
        name: String,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Single<String> =
        repository.createSubject(name, description, latitude, longitude)

    fun getSubjects(): Observable<List<Subject>> = repository.getSubjectsFromFb()

}