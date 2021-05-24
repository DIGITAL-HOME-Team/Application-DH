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
        val humidite_plant_reel: Int?,
        val valeur_luminosite:Int?,
        val ph : Int?,
        val humidite_air : Int?,
        val nombre_arrosage :Int?,
        val volume_eau_journalier : Int?,
        val concentration_co2:Int?,
        val concentration_lgp:Int?,
        val concentration_fumee:Int?,
        val humidite_plant_consigne:Int?,
        val choix_plant:Boolean?,
        val luminosite_consgine:Int?


){}


