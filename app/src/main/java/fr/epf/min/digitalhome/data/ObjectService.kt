package fr.epf.min.digitalhome.data

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


data class Object(val name: String,
                  val type : String,
                  val id:Int,
                  val temp_reel:Int,
                  val temp_consigne:Int,
                    val allumer_light:Boolean,
                    val pourcentage_eau_plant:Int,
                    val valeur_luminosite:Int,
                    val actif_volet:Int)

