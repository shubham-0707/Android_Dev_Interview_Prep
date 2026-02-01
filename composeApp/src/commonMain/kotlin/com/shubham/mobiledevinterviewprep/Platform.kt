package com.shubham.mobiledevinterviewprep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform