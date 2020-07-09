package com.bokugan.pexchange.interfaceadapters.repositories.datasources

import com.bokugan.pexchange.interfaceadapters.repositories.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result
import io.reactivex.Single

interface RemoteCurrencyDataSource {
    fun fetch(): Single<Result<List<HistoricalCurrencyPair>>>
}