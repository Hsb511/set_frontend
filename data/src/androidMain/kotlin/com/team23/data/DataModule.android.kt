package com.team23.data

import com.team23.data.datastore.SetDataStore
import com.team23.data.datastore.SetDataStoreImpl
import org.koin.dsl.module

internal actual fun platformModule() = module {
    single { SetDataStoreImpl() as SetDataStore }
}
