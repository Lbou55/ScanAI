package com.example.scanai.data.repository

import com.example.scanai.data.local.ScanDao
import com.example.scanai.data.local.ScanEntity
import kotlinx.coroutines.flow.Flow

class ScanRepository(private val dao: ScanDao) {

    val allScans: Flow<List<ScanEntity>> = dao.getAllScans()

    suspend fun insertScan(scan: ScanEntity) = dao.insertScan(scan)

    suspend fun deleteScan(scan: ScanEntity) = dao.deleteScan(scan)

    suspend fun getScanById(id: Int): ScanEntity? = dao.getScanById(id)
}

