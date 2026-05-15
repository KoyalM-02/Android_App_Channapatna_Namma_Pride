package com.nammapride.channapatna.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToyEntity::class], version = 1, exportSchema = false)
abstract class NammaPrideDatabase : RoomDatabase() {
    abstract fun toyDao(): ToyDao

    companion object {
        @Volatile
        private var instance: NammaPrideDatabase? = null

        fun get(context: Context): NammaPrideDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NammaPrideDatabase::class.java,
                    "namma_pride.db"
                ).build().also { instance = it }
            }
        }
    }
}
