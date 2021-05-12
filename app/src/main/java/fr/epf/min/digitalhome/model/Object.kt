package fr.epf.min.digitalhome.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type{

    LIGHT,WINDOW,PLANT,HEATER

}

@Entity(tableName = "objects")
data class Object(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        val name:String,
        val type: Type,
        val Temp: Int?,
        val allumer: Boolean?,
        val actif: Boolean?


){}


