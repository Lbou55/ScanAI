package com.example.scanai.data.repository

import com.example.scanai.data.local.ScanDao
import com.example.scanai.data.local.ScanEntity
import com.example.scanai.data.model.ClassificationResult
import com.example.scanai.data.remote.ApiClient
import com.example.scanai.data.remote.ApiKeys
import com.example.scanai.data.remote.HuggingFaceService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ScanRepository(
    private val dao: ScanDao,
    private val api: HuggingFaceService = ApiClient.huggingFaceService
) {
    val allScans: Flow<List<ScanEntity>> = dao.getAllScans()

    suspend fun insertScan(scan: ScanEntity) = dao.insertScan(scan)

    suspend fun deleteScan(scan: ScanEntity) = dao.deleteScan(scan)

    suspend fun getScanById(id: Int): ScanEntity? = dao.getScanById(id)

    suspend fun classifyImage(imageFile: File): ClassificationResult {
        val bytes = imageFile.readBytes()
            .toRequestBody("application/octet-stream".toMediaType())
        val results = api.classifyImage(
            "Bearer ${ApiKeys.HF_API_KEY}",
            bytes
        )
        return results.first()
    }
}