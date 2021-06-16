package com.example.svrmaps.network.repository

import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.model.subject.Subject
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Single
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : ExchangeRepository {

    private var myRef = database.getReference("exchange")
    private var listRef = database.getReference("exchange").child("list")

    override fun createExchange(exchange: Exchange) : Single<String> {
        val key = listRef.push().key ?: "null"
        myRef.child(key).setValue(exchange)
        return Single.just(key)
    }

}