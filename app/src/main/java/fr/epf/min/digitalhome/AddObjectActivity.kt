package fr.epf.min.digitalhome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Dao
import androidx.room.Room
import fr.epf.min.digitalhome.data.ObjectDao
import fr.epf.min.digitalhome.data.ObjectDataBase
import fr.epf.min.digitalhome.data.ObjectService
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_add_heater.*
import kotlinx.android.synthetic.main.activity_add_light.*

import kotlinx.android.synthetic.main.activity_add_plant.*
import kotlinx.android.synthetic.main.activity_add_window.*
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
    var ip="http://192.168.1.158:5000/"
    lateinit var service: ObjectService
    var type=""
    lateinit var types:Type

    var idobject by Delegates.notNull<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type").toString()
        }

        setContentView(when (type) {
            "HEATER" -> {R.layout.activity_add_heater }
            "WINDOW" -> {R.layout.activity_add_window }
            "LIGHT"-> {R.layout.activity_add_light }
            "PLANT" -> {R.layout.activity_add_plant }
            else -> R.layout.activity_add_plant
        })


        types =(
                when (type) {
                    "HEATER" -> {Type.HEATER }
                    "WINDOW" -> {Type.WINDOW }
                    "LIGHT"-> {Type.LIGHT }
                    "PLANT" -> {Type.PLANT }
                    else -> Type.PLANT
                })


        Dao()
        when (type) {
            "HEATER" -> {
                add_heater()
            }
            "WINDOW" -> {
                add_window()
            }
            "LIGHT" -> {
                add_light()

            }
            "PLANT" -> {
                add_plant()
            }
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

    fun add_plant(){
        plant_add_button.setOnClickListener{
            var name =plant_name_edittext.text.toString()
            var humidite_plant_consigne= humidite_plant_consigne_edittext.text.toString()
            var volume_eau_jouarnalier=volume_eau_journalier_edittext.text.toString()
            var nombre_arrosage=nombre_arrosage_edittext.text.toString()

            val `object` = Object(null,
                    "${name}",
                    types,
                    false,
                    null,
                   null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    nombre_arrosage.toInt(),
                    volume_eau_jouarnalier.toInt(),
                    null,
                    null,
                    null,
                    humidite_plant_consigne.toInt(),
                    null,
            null)

            runBlocking {
                idobject=objectDao.addObject(`object`)
                ConnexionBaseMongoDb()
                val objectToMongoDB="{\"json\":{\"name\":\"${name}\",\"type\":\"${this@AddObjectActivity.type}\",\"allumer_light\": false,\"actif_volet\": 0,\"temp_consigne\": 0,\"temp_reel\": 0,\"humidite_plant_reel\": 0,\"valeur_luminosite\": 0, \"id\": ${idobject},\"humidite_sol\":0,\"ph\":0,\"humidite_air\":0,\"nombre_arrosage\":${nombre_arrosage.toInt()},\"volume_eau_journalier\":${volume_eau_jouarnalier.toInt()},\"concentration_co2\":0,\"concentration_lgp\":0,\"concentration_fumee\":0,\"humidite_plant_consigne\":${humidite_plant_consigne.toInt()},\"luminosite_consigne\":0,\"automatique\":true}}"

                val requestBody = objectToMongoDB.toRequestBody("application/json".toMediaTypeOrNull())
                service.createObject(requestBody)

            }

            finish()


        }
    }
    fun add_window(){
        window_add_button.setOnClickListener {
        var name =window_name_edittext.text.toString()
            var valeur_luminosite_consgine=window_luminosite_consigne_edittext.text.toString()

        val `object` = Object(null,
                "${name}",
                types,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        valeur_luminosite_consgine.toInt()
        )

        runBlocking {
            idobject=objectDao.addObject(`object`)
            ConnexionBaseMongoDb()
            val objectToMongoDB="{\"json\":{\"name\":\"${name}\",\"type\":\"${this@AddObjectActivity.type}\",\"allumer_light\": false,\"actif_volet\": 0,\"temp_consigne\": 0,\"temp_reel\": 0,\"humidite_plant_reel\": 0,\"valeur_luminosite\": 0, \"id\": ${idobject},\"humidite_sol\":0,\"ph\":0,\"humidite_air\":0,\"nombre_arrosage\":0,\"volume_eau_journalier\":0,\"concentration_co2\":0,\"concentration_lgp\":0,\"concentration_fumee\":0,\"humidite_plant_consigne\":0,\"luminosite_consigne\":${valeur_luminosite_consgine.toInt()},\"automatique\":false}}"

            val requestBody = objectToMongoDB.toRequestBody("application/json".toMediaTypeOrNull())
            service.createObject(requestBody)

        }

        finish()


    }}
    fun add_heater(){
        heater_add_button.setOnClickListener{
            var name =heater_name_edittext.text.toString()
            var temp_consigne=heater_temp_consigne_edittext.text.toString()

            val `object` = Object(null,
                    "${name}",
                    types,
                    false,
                    null,
                    temp_consigne.toInt(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
            null)

            runBlocking {
                idobject=objectDao.addObject(`object`)
                ConnexionBaseMongoDb()
                val objectToMongoDB="{\"json\":{\"name\":\"${name}\",\"type\":\"${this@AddObjectActivity.type}\",\"allumer_light\": false,\"actif_volet\": 0,\"temp_consigne\": ${temp_consigne.toInt()},\"temp_reel\": 0,\"humidite_plant_reel\": 0,\"valeur_luminosite\": 0, \"id\": ${idobject},\"humidite_sol\":0,\"ph\":0,\"humidite_air\":0,\"nombre_arrosage\":0,\"volume_eau_journalier\":0,\"concentration_co2\":0,\"concentration_lgp\":0,\"concentration_fumee\":0,\"humidite_plant_consigne\":0,\"luminosite_consigne\":0,\"automatique\":null}}"

                val requestBody = objectToMongoDB.toRequestBody("application/json".toMediaTypeOrNull())
                service.createObject(requestBody)

            }

            finish()


        }
    }

    fun add_light(){  light_add_button.setOnClickListener{
        var name =light_name_edittext.text.toString()

        val `object` = Object(null,
                "${name}",
                types,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        null)

        runBlocking {
            idobject=objectDao.addObject(`object`)
            ConnexionBaseMongoDb()
            val objectToMongoDB="{\"json\":{\"name\":\"${name}\",\"type\":\"${this@AddObjectActivity.type}\",\"allumer_light\": false,\"actif_volet\": 0,\"temp_consigne\": 0,\"temp_reel\": 0,\"humidite_plant_reel\": 0,\"valeur_luminosite\": 0, \"id\": ${idobject},\"humidite_sol\":0,\"ph\":0,\"humidite_air\":0,\"nombre_arrosage\":0,\"volume_eau_journalier\":0,\"concentration_co2\":0,\"concentration_lgp\":0,\"concentration_fumee\":0,\"humidite_plant_consigne\":0,\"luminosite_consigne\":0,\"automatique\":null}}"

            val requestBody = objectToMongoDB.toRequestBody("application/json".toMediaTypeOrNull())
            service.createObject(requestBody)

        }

        finish()


    }}


}