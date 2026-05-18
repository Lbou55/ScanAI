package com.example.scanai.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanai.data.local.ScanDatabase
import com.example.scanai.data.local.ScanEntity
import com.example.scanai.data.model.UiState
import com.example.scanai.data.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScanRepository(
        ScanDatabase.getInstance(application).scanDao()
    )

    private var imageCapture: ImageCapture? = null

    private val _uiState = MutableStateFlow<UiState<ScanEntity>>(UiState.Idle)
    val uiState: StateFlow<UiState<ScanEntity>> = _uiState

    fun setImageCapture(capture: ImageCapture) {
        imageCapture = capture
    }

    fun capturePhoto(context: Context) {
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        _uiState.value = UiState.Loading

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(file).toString()
                    viewModelScope.launch {
                        val scan = ScanEntity(
                            imageUri = uri,
                            label = "Pending",
                            confidence = 0f
                        )
                        repository.insertScan(scan)
                        _uiState.value = UiState.Success(scan)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.value = UiState.Error(exception.message ?: "Erreur capture")
                }
            }
        )
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}