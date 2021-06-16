package com.example.svrmaps.interactor

import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.network.repository.ExchangeRepository
import io.reactivex.Single
import javax.inject.Inject

class ExchangeInteractor @Inject constructor(
    private val repository: ExchangeRepository
) {

    fun createExchangeOffer(exchange: Exchange) : Single<String> = repository.createExchange(exchange)
}