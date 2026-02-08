package com.shubham.mobiledevinterviewprep.platform

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

@Composable
actual fun rememberImagePicker(onResult: (ByteArray?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) {
            onResult(null)
        } else {
            onResult(readBytes(context, uri))
        }
    }
    return remember { { launcher.launch("image/*") } }
}

private fun readBytes(context: Context, uri: android.net.Uri): ByteArray? {
    return try {
        context.contentResolver.openInputStream(uri)?.use(InputStream::readBytes)
    } catch (_: Exception) {
        null
    }
}
