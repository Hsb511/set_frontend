package com.team23.set

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.team23.ui.App
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val greeting = remember { Greeting().greet() }
    App(greeting)
}
