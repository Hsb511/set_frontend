package com.team23.set

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform