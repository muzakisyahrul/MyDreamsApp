package mobile.muzaki.mydreamsapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Keinginan (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val nama : String,
    val deskripsi : String,
    val harga : Double,
    val terpenuhi : Double?=0.0,
)