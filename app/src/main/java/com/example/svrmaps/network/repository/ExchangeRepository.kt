package com.example.svrmaps.network.repository

import com.example.svrmaps.model.exchange.Exchange
import io.reactivex.Single

interface ExchangeRepository {

    fun createExchange(exchange: Exchange) : Single<String>
}