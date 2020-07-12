package com.bokugan.pexchange.web

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("pubinfo?exchange&json&coursid=11")
    fun getCurrencyRates(): Single<Response<List<CurrencyPairWeb>>>
}