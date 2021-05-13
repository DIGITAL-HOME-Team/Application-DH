package fr.epf.min.digitalhome.data

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ObjectService {
    @GET("/{uri}")
    suspend fun getObjects(@Path("uri") uri:String  ) : GetObjectsResult

    @POST("/setpoint")
    suspend fun postObjects(@Body valeur : RequestBody): Response<ResponseBody>
}


data class GetObjectsResult(val results: List<Object>)

data class Object(val name: String,val type : String)

