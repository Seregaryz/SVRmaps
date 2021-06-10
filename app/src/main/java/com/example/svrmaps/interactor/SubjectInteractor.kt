package com.example.svrmaps.interactor

import com.example.svrmaps.network.repository.SubjectRepository
import io.reactivex.Single
import javax.inject.Inject

class SubjectInteractor @Inject constructor(
    private val repository: SubjectRepository
) {

    fun createSubject(name: String, description: String): Single<String> =
        repository.createSubject(name, description)
}