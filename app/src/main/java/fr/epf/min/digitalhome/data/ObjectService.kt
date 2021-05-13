package fr.epf.min.digitalhome.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ObjectService {
    @GET("/{test}")
    suspend fun getObjects(@Path("test") uri:String  ) : GetObjectsResult

    @POST("/{test}")
    suspend fun postObjects(@Path("test")uri:String) : PostObjects
}

data class PostObjects(val results: List<Object>)

data class GetObjectsResult(val results: List<Object>)

data class Object(val name: String,val type : String)

