package com.bokugan.pexchange.di

import com.bokugan.pexchange.db.LocalCurrencyDataSourceImpl
import com.bokugan.pexchange.interfaceadapters.repositories.*
import com.bokugan.pexchange.repositoryupdater.WorkManagerUpdater
import com.bokugan.pexchange.usecases.boundaries.CurrencySource
import com.bokugan.pexchange.web.RemoteCurrencyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindCurrencySource(repo: CurrencyRepository): CurrencySource

    @Binds
    abstract fun bindRemoteDataSource(
        dataSource: RemoteCurrencyDataSourceImpl
    ): RemoteCurrencyDataSource

    @Binds
    abstract fun bindLocalDataSource(
        data: LocalCurrencyDataSourceImpl
    ): LocalCurrencyDataSource

    @Binds
    abstract fun bindUpdateDelegate(
        updater: WorkManagerUpdater
    ): UpdateDelegate

    @Binds
    abstract fun bindUpdatable(repo: CurrencyRepository): Updatable
}