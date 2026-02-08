package com.shubham.mobiledevinterviewprep.platform

import androidx.compose.runtime.Composable

/**
 * Platform image picker returning image bytes.
 */
@Composable
expect fun rememberImagePicker(onResult: (ByteArray?) -> Unit): () -> Unit
