package com.shubham.mobiledevinterviewprep.platform

import androidx.compose.runtime.Composable

/**
 * Platform-specific profile photo renderer.
 */
@Composable
expect fun ProfilePhoto(bytes: ByteArray?)
