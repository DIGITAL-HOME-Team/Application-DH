package fr.epf.min.digitalhome.data

import androidx.room.PrimaryKey
import fr.epf.min.digitalhome.model.Type
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ObjectService {
    @GET("/{uri}")
    suspend fun getObjects(@Path("uri") uri:String  ) : GetObjectsResult

    @GET("/{uri}/{type}")
    suspend fun getObjectsByType(@Path("uri") uri:String ,@Path("type") type:String)  :  GetObjectsByTypeResult

    @POST("/setpoint")
    suspend fun postSetpoint(@Body valeur : RequestBody): Response<ResponseBody>

    @POST("/update")
    suspend fun updateObject(@Body valeur : RequestBody): Response<ResponseBody>

    @PUT("/create")
    suspend fun createObject(@Body valeur : RequestBody): Response<ResponseBody>

    @DELETE("/delete/{id}")
    suspend fun deleteobject(@Path("id") id:String): Response<ResponseBody>


}


data class GetObjectsResult(val results: List<Object>)
data class GetObjectsByTypeResult(val results: List<fr.epf.min.digitalhome.model.Object>)


data class Object(
    val name: String,
    val type : String,
    val id:Int,
    val temp_reel:Int,
    val temp_consigne:Int,
    val allumer_light:Boolean,
    val humidite_plant_reel: Int?,
    val valeur_luminosite:Int,
    val actif_volet:Int,
    val ph : Int,
    val humidite_air : Int,
    val nombre_arrosage :Int,
    val volume_eau_journalier : Int,
    val concentration_co2:Int,
    val concentration_lgp:Int,
    val concentration_fumee:Int,
    val humidite_plant_consigne:Int,
    val automatique:Boolean,
    val luminosite_consgine:Int)







