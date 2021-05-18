package fr.epf.min.digitalhome

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import fr.epf.min.digitalhome.data.ObjectDao
import fr.epf.min.digitalhome.data.ObjectDataBase
import fr.epf.min.digitalhome.data.ObjectService
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_details_light.*
import kotlinx.android.synthetic.main.activity_details_plant.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.properties.Delegates


class DetailsActivity : AppCompatActivity() {

    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    var `object_id` by Delegates.notNull<Int>()
    lateinit var `object_name`: String
   var pourcentage_eau_plant = 0
    var actif_volet =1
    var temp_reel by Delegates.notNull<Int>()
    var temp_consigne by Delegates.notNull<Int>()
    var allumer_light=false
    lateinit var type:String
    var valeur_luminosite by Delegates.notNull<Int>()
    lateinit var changeobject:Object
    var ip="http://192.168.1.35:5000/"
    lateinit var service: ObjectService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        Dao()



        if (intent.hasExtra("object_id")) {
            `object_id` = intent?.getIntExtra("object_id",1)!!
            `object_name` = intent.getStringExtra("object_name").toString()
            valeur_luminosite= intent?.getIntExtra("valeur_luminosite",1)!!
            temp_reel=intent?.getIntExtra("temp_reel",1)!!
            temp_consigne=intent?.getIntExtra("temp_consigne",1)!!
            actif_volet=intent?.getIntExtra("actif_volet",0)
            allumer_light=intent?.getBooleanExtra("allumer_light",false)
            pourcentage_eau_plant=intent?.getIntExtra("pourcentage_eau_plant",0)
            type = intent?.getStringExtra("type").toString()
        }

        setContentView(
                when (type) {
                    "HEATER" -> R.layout.activity_details_heater
                    "WINDOW" -> R.layout.activity_details_window
                    "LIGHT" -> R.layout.activity_details_light
                    "PLANT" -> R.layout.activity_details_plant
                    else -> R.layout.activity_details_plant
                }
                )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (type) {
            "HEATER" -> {}
            "WINDOW" -> {}
            "LIGHT" -> affiche_detail_light()
            "PLANT" -> affiche_detail_plant()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_object,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_object_action -> {
                AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Voulez-vous vraiment supprimer la/le objet?")
                        .setPositiveButton("Oui"){
                            _,_ ->


                            runBlocking {
                                val objects = objectDao.findByid(`object_id`)
                                objectDao.deleteObject(objects)
                                ConnexionBaseMongoDb()
                                val object_id_to_string:String=`object_id`.toString()


                            service.deleteobject(object_id_to_string)}
                            finish()
                            Toast.makeText(this,"Objet supprimé", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Non"){
                            _,_ ->
                            Log.d("epf","close dialog")
                        }
                        .show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun Dao(){
        //accés a la base
        database = Room.databaseBuilder(
            this, ObjectDataBase::class.java, "objects-db"

        ).build()
        objectDao = database.getObjectDao()
    }
    fun affiche_detail_plant(){
        change_object()
        plant_name_textview?.text= "${`object_name`}"

    }
    fun affiche_detail_light(){

        change_object()

        light_name_textview?.text="${`object_name`}"
        valeur_luminosite_TextView?.text="${valeur_luminosite}"


    }

    fun change_object(){
        changeobject = Object(object_id, object_name,
                Type.LIGHT,
                allumer_light,
                actif_volet,
                temp_consigne,
                temp_reel,
                pourcentage_eau_plant,
                valeur_luminosite)
        runBlocking { objectDao.changeByName(changeobject) }

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