package com.example.scanai.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScanEntity::class], version = 1)
abstract class ScanDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao

    companion object {
        @Volatile private var INSTANCE: ScanDatabase? = null

        fun getInstance(context: Context): ScanDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ScanDatabase::class.java, "scan_db")
                    .build()
                    .also { INSTANCE = it }
            }
    }
}