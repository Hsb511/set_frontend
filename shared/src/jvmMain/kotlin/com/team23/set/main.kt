package com.team23.set

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.team23.data.dataModule
import com.team23.domain.domainModule
import com.team23.ui.App
import com.team23.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun main() {
    startKoin {
        modules(
            uiModule,
            domainModule,
            dataModule,
        )
    }

    return application {
        Window(
            onCloseRequest = {
                stopKoin()
                exitApplication()
            }
        ) {
            App()
        }
    }
}
