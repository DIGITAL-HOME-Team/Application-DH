package fr.epf.min.digitalhome.data

import androidx.room.*
import fr.epf.min.digitalhome.model.Object


@Dao
interface ObjectDao {

    @Query("select * from objects")
    suspend fun getAllObjects(): List<Object>

    @Query("SELECT * FROM objects WHERE type IN (:Type)")
    suspend fun getAllObjectsOfType(Type: String): List<Object>

    @Query("SELECT * FROM objects WHERE name LIKE :name  LIMIT 1")
    suspend fun findByName(name: String): Object

    @Query("SELECT * FROM objects WHERE id LIKE :id  LIMIT 1")
    suspend fun findByid(id: Int?): Object



    @Insert
    suspend fun addObject(`object`:Object)

    @Delete
    suspend fun deleteObject(`object`:Object)

    @Update
    suspend fun changeByName(`object`:Object)

}