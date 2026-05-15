package com.nammapride.channapatna.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toys")
data class ToyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val price: String,
    val rating: String,
    val artisan: String,
    val image: String = ""
)
