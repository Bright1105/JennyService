package com.example.jennyserviceapp.ui.screens.uploadfeed

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = viewModel(factory = ServiceViewModelProvider.Factory),
) {
    val selectVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        feedViewModel.videoUri.value = it
    }

    val scope = CoroutineScope(Dispatchers.IO)

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val mContent = LocalContext.current

    val mExoPlayer = remember(mContent) {
        ExoPlayer.Builder(mContent)
            .build()
            .also {
                it.trackSelectionParameters = it.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
        AndroidView(
            factory = { content ->
                PlayerView(content).also { player ->
                    player.player = mExoPlayer
                    feedViewModel.videoUri.value?.let {
                        MediaItem.fromUri(
                            it
                        )
                    }?.let { player.player?.setMediaItem(it) }
                    player.player?.prepare()
                }
            },
            update = { playerUpdate ->
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        playerUpdate.onPause()
                        playerUpdate.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        playerUpdate.onResume()
                    }

                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_300))
                .padding(dimensionResource(R.dimen.dp_10))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
        Button(
            onClick = { selectVideoLauncher.launch("video/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.selectVideo),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
        Button(
            onClick = {
                scope.launch {
                    feedViewModel.addVideoToStorage()
                    feedViewModel.addFeedToFireStore()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.saveVideoToStorage),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}