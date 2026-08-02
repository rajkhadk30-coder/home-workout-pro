package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.data.model.Language
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = true
            }
        } else {
            Log.e("TtsHelper", "TextToSpeech Initialization failed")
        }
    }

    fun speak(text: String, language: Language) {
        if (!isInitialized || text.isBlank()) return
        
        try {
            if (language == Language.NEPALI) {
                val nepaliLocale = Locale("ne", "NP")
                val res = tts?.setLanguage(nepaliLocale)
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.US)
                }
            } else {
                tts?.setLanguage(Locale.US)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "WorkoutVoiceCoach")
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error speaking TTS: ${e.message}")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
