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
        val allumer_light: Boolean?,
        val actif_volet: Int?,
        val temp_consigne: Int?,
        val temp_reel: Int?,
        val pourcentage_eau_plant: Int?,
    val valeur_luminosite:Int?


){}


