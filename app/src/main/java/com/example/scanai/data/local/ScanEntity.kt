package com.example.scanai.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String,
    val label: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
)

