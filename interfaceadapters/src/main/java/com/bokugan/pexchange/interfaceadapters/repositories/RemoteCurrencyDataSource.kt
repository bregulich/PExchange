package com.bokugan.pexchange.interfaceadapters.repositories

import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result
import io.reactivex.Single

interface RemoteCurrencyDataSource {
    fun fetch(): Single<Result<List<HistoricalCurrencyPair>>>
}