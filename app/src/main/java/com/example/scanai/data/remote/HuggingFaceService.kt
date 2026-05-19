package com.example.scanai.data.remote

import com.example.scanai.data.model.ClassificationResult
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HuggingFaceService {
    @POST("models/google/vit-base-patch16-224")
    suspend fun classifyImage(
        @Header("Authorization") token: String,
        @Body imageBytes: RequestBody
    ): List<ClassificationResult>
}