package com.example.data.gemini

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class GeminiModel(val modelId: String, val displayName: String, val description: String) {
    GENERAL("gemini-3.5-flash", "Gemini 3.5 Flash", "General Fitness & Diet Coach"),
    COMPLEX("gemini-3.1-pro-preview", "Gemini 3.1 Pro", "Deep Workout & Physiology Analysis"),
    FAST("gemini-3.1-flash-lite-preview", "Gemini 3.1 Flash Lite", "Ultra-Fast Quick Answers")
}

enum class ChatSender {
    USER, BOT
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelUsed: String = "Gemini AI",
    val isError: Boolean = false
)

class GeminiChatService {
    private val TAG = "GeminiChatService"
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val systemInstruction = """
        You are Home Workout Pro's Elite AI Personal Trainer & Nutrition Coach.
        Your goal is to provide expert, safe, science-backed fitness routines, bodyweight exercises, diet plans, macro breakdowns, and motivational advice.
        Format your answers cleanly using Markdown bullet points, clear headings, and concise steps.
    """.trimIndent()

    suspend fun sendMessage(
        history: List<ChatMessage>,
        userPrompt: String,
        selectedModel: GeminiModel = GeminiModel.GENERAL
    ): ChatMessage = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext ChatMessage(
                sender = ChatSender.BOT,
                text = "AI Key setup required. Please enter your Gemini API Key in AI Studio Secrets to chat with your personal AI trainer.",
                modelUsed = selectedModel.displayName,
                isError = true
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/${selectedModel.modelId}:generateContent?key=$apiKey"

        try {
            val rootJson = JSONObject()

            // System Instruction
            val systemContent = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            }
            rootJson.put("systemInstruction", systemContent)

            // Contents history
            val contentsArray = JSONArray()

            // Add previous history turns
            history.takeLast(10).forEach { msg ->
                val role = if (msg.sender == ChatSender.USER) "user" else "model"
                val contentObj = JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
                }
                contentsArray.put(contentObj)
            }

            // Add current user prompt
            val currentContent = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
            }
            contentsArray.put(currentContent)

            rootJson.put("contents", contentsArray)

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini API Error Code ${response.code}: $responseBody")
                return@withContext ChatMessage(
                    sender = ChatSender.BOT,
                    text = "I encountered an issue generating a response (${response.code}). Please try again in a moment.",
                    modelUsed = selectedModel.displayName,
                    isError = true
                )
            }

            val jsonRes = JSONObject(responseBody)
            val candidates = jsonRes.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val responseText = parts?.optJSONObject(0)?.optString("text") ?: "No response generated."

            ChatMessage(
                sender = ChatSender.BOT,
                text = responseText,
                modelUsed = selectedModel.displayName
            )
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini API request: ${e.message}", e)
            ChatMessage(
                sender = ChatSender.BOT,
                text = "Network or API connection error: ${e.localizedMessage ?: "Unknown error"}",
                modelUsed = selectedModel.displayName,
                isError = true
            )
        }
    }
}
