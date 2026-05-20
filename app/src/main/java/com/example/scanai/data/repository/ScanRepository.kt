package com.example.scanai.data.repository

import android.util.Base64
import com.example.scanai.data.local.ScanDao
import com.example.scanai.data.local.ScanEntity
import com.example.scanai.data.remote.ApiClient
import com.example.scanai.data.remote.ApiKeys
import com.example.scanai.data.remote.GeminiContent
import com.example.scanai.data.remote.GeminiInlineData
import com.example.scanai.data.remote.GeminiPart
import com.example.scanai.data.remote.GeminiRequest
import com.example.scanai.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import java.io.File

class ScanRepository(
    private val dao: ScanDao,
    private val api: GeminiService = ApiClient.geminiService
) {
    val allScans: Flow<List<ScanEntity>> = dao.getAllScans()

    suspend fun insertScan(scan: ScanEntity) = dao.insertScan(scan)

    suspend fun deleteScan(scan: ScanEntity) = dao.deleteScan(scan)

    suspend fun getScanById(id: Int): ScanEntity? = dao.getScanById(id)

    suspend fun classifyImage(imageFile: File): Pair<String, Float> {
        val imageBytes = imageFile.readBytes()
        val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "Identifie l'objet principal dans cette image. " +
                                    "Réponds en français avec juste le nom de l'objet en 1 à 3 mots maximum. " +
                                    "Ne donne aucune explication, juste le nom."
                        ),
                        GeminiPart(
                            inline_data = GeminiInlineData(
                                mime_type = "image/jpeg",
                                data = base64Image
                            )
                        )
                    )
                )
            )
        )

        var lastError: Exception? = null
        repeat(3) { attempt ->
            try {
                val response = api.analyzeImage(ApiKeys.GEMINI_API_KEY, request)
                val label = response.candidates
                    .firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text
                    ?.trim()
                    ?: "Objet inconnu"
                return Pair(label, 1.0f)
            } catch (e: Exception) {
                lastError = e
                if (e.message?.contains("429") == true) {
                    kotlinx.coroutines.delay((attempt + 1) * 5000L)
                } else {
                    throw e
                }
            }
        }
        throw lastError ?: Exception("Erreur inconnue")
    }
}