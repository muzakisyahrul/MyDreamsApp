package mobile.muzaki.mydreamsapp.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KeinginanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun tambahKeinginan(keinginan: Keinginan)

    @Update
    fun updateKeinginan(keinginan: Keinginan)

    @Query("SELECT * FROM Keinginan ORDER BY id DESC")
    fun getKeinginan() : LiveData<List<Keinginan>>

    @Query("SELECT * FROM Keinginan WHERE id=:keinginan_id")
    fun getKeinginan(keinginan_id: Int) : LiveData<List<Keinginan>>

    @Query("SELECT * FROM Keinginan WHERE harga>terpenuhi")
    fun getKeinginanBlmTerpenuhi() : LiveData<List<Keinginan>>

    @Query("SELECT * FROM Keinginan WHERE terpenuhi>=harga")
    fun getKeinginanTerpenuhi() : LiveData<List<Keinginan>>

    @Delete
    fun deleteKeinginan(keinginan: Keinginan)


}