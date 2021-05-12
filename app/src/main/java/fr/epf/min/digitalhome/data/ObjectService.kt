package fr.epf.min.digitalhome.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ObjectService {
    @GET("/{test}")
    suspend fun getObjects(@Path("test") uri:String  ) : GetObjectsResult
}

data class GetObjectsResult(val results: List<Object>)

data class Object(val name: String,val type : String)

