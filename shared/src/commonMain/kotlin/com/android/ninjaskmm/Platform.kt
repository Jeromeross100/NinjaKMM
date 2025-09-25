package com.android.ninjaskmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform