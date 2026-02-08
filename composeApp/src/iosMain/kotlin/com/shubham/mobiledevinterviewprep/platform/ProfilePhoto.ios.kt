package com.shubham.mobiledevinterviewprep.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Image as SkiaImage

@Composable
actual fun ProfilePhoto(bytes: ByteArray?) {
    if (bytes == null) return
    val bitmap = SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
    Image(
        bitmap = bitmap,
        contentDescription = "Profile photo",
        modifier = Modifier.size(96.dp)
    )
}
