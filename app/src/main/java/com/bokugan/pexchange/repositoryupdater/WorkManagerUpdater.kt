package com.bokugan.pexchange.repositoryupdater

import com.bokugan.pexchange.interfaceadapters.repositories.Updatable
import com.bokugan.pexchange.interfaceadapters.repositories.UpdateDelegate
import io.reactivex.Completable
import javax.inject.Inject

class WorkManagerUpdater @Inject constructor() : UpdateDelegate {

    override fun delegateUpdates(updatable: Updatable): Completable {


        return Completable.complete()
    }
}