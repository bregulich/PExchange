package com.bokugan.pexchange.di

import android.content.Context
import androidx.room.Room
import com.bokugan.pexchange.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DBModule {

    @DatabaseName
    @Provides
    fun provideDBName() = "pexchange"

    @Provides
    @Singleton
    fun provideDB(
        @ApplicationContext context: Context,
        @DatabaseName dbName: String
    ) = Room.databaseBuilder(context, Database::class.java, dbName).build()

    @Provides
    @Singleton
    fun provideCurrencyPairDao(db: Database) = db.currencyPairDao()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName