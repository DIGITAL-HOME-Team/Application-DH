package fr.epf.min.digitalhome

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import fr.epf.min.digitalhome.data.ObjectDao
import fr.epf.min.digitalhome.data.ObjectDataBase
import fr.epf.min.digitalhome.data.ObjectService
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_add_object.*
import kotlinx.android.synthetic.main.activity_object.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.properties.Delegates

class AddObjectActivity : AppCompatActivity() {


    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    var ip="http://192.168.1.35:5000/"
    lateinit var service: ObjectService
    lateinit var object_type:String
    var idobject by Delegates.notNull<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)


        object_add_button.setOnClickListener{
            var name =object_name_edittext.text.toString()

            var object_id= object_type_RadioGroup.checkedRadioButtonId as Int

            var type :Type=(
            when (object_id) {
                object_heater_radiobutton.id  -> {Type.HEATER }
                object_window_radiobutton.id -> {Type.WINDOW }
                object_light_radiobutton.id-> {Type.LIGHT }
                object_plant_radiobutton.id -> {Type.PLANT }
                else -> Type.PLANT
            })
            object_type=(
            when (object_id) {
                object_heater_radiobutton.id  -> {"HEATER" }
                object_window_radiobutton.id -> {"WINDOW" }
                object_light_radiobutton.id-> {"LIGHT" }
                object_plant_radiobutton.id -> {"PLANT" }
                else -> "PLANT"
            })

            Dao()

            val `object` = Object(null,
                    "${name}",
                    type,
                    null,
                    null,
                    null,
                    24,
                    24, 50)

            runBlocking {
                idobject=objectDao.addObject(`object`)
                ConnexionBaseMongoDb()
                val objectToMongoDB="{\"json\":{\"name\":\"${name}\",\"type\":\"${object_type}\",\"allumer_light\": false,\"actif_volet\": 0,\"temp_consigne\": 24,\"temp_reel\": 24,\"pourcentage_eau_plant\": 50,\"valeur_luminosite\": 50, \"id\": ${idobject}}}"

                val requestBody = objectToMongoDB.toRequestBody("application/json".toMediaTypeOrNull())
                service.createObject(requestBody)

                }

            finish()


        }
}


    private fun Dao(){
        //accés a la


        database = Room.databaseBuilder(
            this, ObjectDataBase::class.java, "objects-db"

        ).build()


        objectDao = database.getObjectDao()
    }
    fun ConnexionBaseMongoDb(){
        runBlocking {
            val retrofit = Retrofit.Builder()
                    .baseUrl(ip)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            service = retrofit.create(ObjectService::class.java)
        }

    }


}