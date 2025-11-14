package com.example.solarexplorer.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Small helper that exposes a suspendable speak function.
 * - speakAwait(text) will suspend until the utterance is finished (or error).
 * - stop() stops current speech.
 * - shutdown() release resources.
 */
class TTSHelper(context: Context) {

    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    @Volatile
    private var ready = false

    init {
        tts = TextToSpeech(appContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                ready = true
            }
        }
    }

    fun isReady(): Boolean = ready

    fun isSpeaking(): Boolean = tts?.isSpeaking ?: false

    /**
     * Stops current speaking immediately.
     */
    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) { }
    }

    /**
     * Call this when your activity / composable is destroyed to free TTS resources.
     */
    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) { }
        tts = null
        ready = false
    }

    /**
     * Speak `text` and suspend until the utterance finishes.
     * Uses UtteranceProgressListener and a unique utteranceId.
     */
    suspend fun speakAwait(text: String) {
        if (!ready) return

        suspendCancellableCoroutine<Unit> { cont ->
            val utteranceId = UUID.randomUUID().toString()

            val listener = object : UtteranceProgressListener() {
                override fun onStart(uttId: String) { /* no-op */ }

                override fun onDone(uttId: String) {
                    if (uttId == utteranceId && cont.isActive) {
                        cont.resume(Unit)
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(uttId: String) {
                    if (uttId == utteranceId && cont.isActive) {
                        cont.resume(Unit) // treat error as finished so tour continues
                    }
                }

                // Newer API: onError(utteranceId, errorCode) exists but overriding above is fine.
            }

            // set listener and speak
            tts?.setOnUtteranceProgressListener(listener)
            try {
                // third argument is utteranceId (requires API 21+), safe if minSdk >= 21
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            } catch (e: Exception) {
                // If speak throws, resume to avoid hanging
                if (cont.isActive) cont.resumeWithException(e)
            }

            // If coroutine is cancelled while waiting, stop the speech
            cont.invokeOnCancellation {
                try {
                    tts?.stop()
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Convenience: speak list of texts sequentially with small delay in between.
     * The coroutine that calls this can be cancelled to stop the tour immediately.
     */
    suspend fun speakSequentially(texts: List<String>, delayBetweenMs: Long = 300L) {
        for (text in texts) {
            // if the coroutine was cancelled externally, this will throw and exit
            speakAwait(text)
            if (delayBetweenMs > 0) delay(delayBetweenMs)
        }
    }
}
