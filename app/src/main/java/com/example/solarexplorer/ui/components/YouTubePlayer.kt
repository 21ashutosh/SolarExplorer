//package com.example.solarexplorer.ui.components
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.viewinterop.AndroidView
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
//
//@Composable
//fun YouTubePlayer(videoId: String, modifier: Modifier = Modifier) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            YouTubePlayerView(context).apply {
//                lifecycleOwner.lifecycle.addObserver(this)
//
//                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                    override fun onReady(youTubePlayer: YouTubePlayer) {
//                        youTubePlayer.cueVideo(videoId, 0f)
//                    }
//                })
//            }
//        }
//    )
//}
