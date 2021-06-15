package com.example.svrmaps.interactor

import com.example.svrmaps.model.user.UserAccount
import com.example.svrmaps.model.user.UserSignUpItem
import com.example.svrmaps.network.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
){

    fun signIn(email: String, password: String): Single<UserAccount> =
        Single.just(UserAccount(email, userRepository.signIn(email, password)))

    fun createAccount(email: String, password: String): Single<UserAccount> =
        Single.just(UserAccount(email, userRepository.createAccount(email, password)))

    fun signOut() = userRepository.signOut()
}