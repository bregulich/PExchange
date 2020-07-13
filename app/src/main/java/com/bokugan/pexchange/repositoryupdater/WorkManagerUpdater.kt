package com.bokugan.pexchange.repositoryupdater

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import com.bokugan.pexchange.interfaceadapters.repositories.Updatable
import com.bokugan.pexchange.interfaceadapters.repositories.UpdateDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "PEXCHANGE_UPDATE_WORK"

class WorkManagerUpdater @Inject constructor(
    @ApplicationContext private val context: Context
) :
    UpdateDelegate {

    override fun delegateUpdates(updatable: Updatable): Completable {
        val work = PeriodicWorkRequest
            .Builder(RxUpdater::class.java, 20, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
            .addTag(TAG)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )

        return Completable.complete()
    }
}

class RxUpdater @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updatable: Updatable
) :
    RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return updatable.update().toSingle { Result.success() }
    }
}