package fr.epf.min.digitalhome

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_details_window.*
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
   var humidite_plant_reel = 0
    var actif_volet =1
    var temp_reel by Delegates.notNull<Int>()
    var temp_consigne by Delegates.notNull<Int>()
    var allumer_light=false
    lateinit var type:String
    var valeur_luminosite by Delegates.notNull<Int>()
    var ph by Delegates.notNull<Int>()
    var humidite_air by Delegates.notNull<Int>()
    var nombre_arrosage by Delegates.notNull<Int>()
    var volume_eau_journalier by Delegates.notNull<Int>()
    var concentration_co2 by Delegates.notNull<Int>()
    var concentration_lgp by Delegates.notNull<Int>()
    var concentration_fumee by Delegates.notNull<Int>()
    var humidite_plant_consigne by Delegates.notNull<Int>()
    var luminosite_consgine by Delegates.notNull<Int>()
    lateinit var changeobject:Object

    var automatique by Delegates.notNull<Boolean>()
    var ip="http://192.168.43.108:5000/"
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
            humidite_plant_reel=intent?.getIntExtra("pourcentage_eau_plant",0)
            type = intent?.getStringExtra("type").toString()

            ph =intent?.getIntExtra("ph",0)
            humidite_air =intent?.getIntExtra("humidite_air",0)
            nombre_arrosage =intent?.getIntExtra("nombre_arrosage",0)
            volume_eau_journalier =intent?.getIntExtra("volume_eau_journalier",0)
            concentration_co2 =intent?.getIntExtra("concentration_co2",0)
            concentration_lgp= intent?.getIntExtra("concentration_lgp",0)
            concentration_fumee= intent?.getIntExtra("concentration_fumee",0)
            humidite_plant_consigne =intent?.getIntExtra("humidite_plant_consigne",0)
            automatique=intent?.getBooleanExtra("automatique",false)
            luminosite_consgine=intent?.getIntExtra("luminosite_consgine",0)
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
            "HEATER" -> affiche_detail_heater()
            "WINDOW" -> affiche_detail_window()
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
        var choix_plant_detail=automatique
        change_object(type,humidite_plant_consigne,nombre_arrosage,volume_eau_journalier,choix_plant_detail,null)
        plant_name_textview?.text= "${`object_name`}"
        ph_textview?.text="${ph}"
        humidite_air_textview?.text="${humidite_air}"
        nombre_arrosage_detail_textview?.text="Nombre d'arrosage : ${nombre_arrosage}"
        volume_eau_journalier_detail_textview?.text="Volume d'eau journalier : ${volume_eau_journalier}"
        concentration_co2_textview?.text="${concentration_co2}"
        concentration_lgp_textview?.text="${concentration_lgp}"
        concentration_fumee_textview?.text="${concentration_fumee}"
        humidite_plant_reel_textview?.text="${humidite_plant_reel}"
        titre_humidite_plant_consigne_detail_textview?.text="Humidité du sol consigne : ${humidite_plant_consigne}"




        Modify_consign_plant_button.setOnClickListener {

            var choix_plant_radiobutton= Plant_choix_RadioGroup.checkedRadioButtonId as Int
            if(choix_plant_radiobutton==plant_automatique_radiobutton.id){
                choix_plant_detail=true
                Log.d("epf","true")
            }
            if(choix_plant_radiobutton==plant_consigne_radiobutton.id){
                choix_plant_detail=false
                Log.d("epf","false")
            }
          var  new_humidite_plant_consign = humidite_plant_consigne_detail_edittext.text.toString()
            var new_Nombre_Arossage =nombre_arrosage_detail_edittext.text.toString()
           var  new_Volume_Eau_Journalier =volume_eau_journalier_detail_edittext.text.toString()
            change_object(type,new_humidite_plant_consign.toInt(),new_Nombre_Arossage.toInt(),new_Volume_Eau_Journalier.toInt(),choix_plant_detail,null)
            finish()
        }


    }
    fun affiche_detail_light(){

        change_object(type,null,null,null,null,null)

        light_name_detail_textview?.text="${`object_name`}"
        valeur_luminosite_detail_TextView?.text="${valeur_luminosite}"

    }

    fun affiche_detail_heater(){

    }
    fun affiche_detail_window(){

        var choix_window_detail=automatique
        change_object(type,null,null,null,choix_window_detail,luminosite_consgine)
        window_name_detail_textview?.text= "${`object_name`}"


        Modify_consign_window_button.setOnClickListener {

            var choix_window_radiobutton= window_choix_RadioGroup.checkedRadioButtonId as Int
            if(choix_window_radiobutton==window_automatique_radiobutton.id){
                choix_window_detail=true
                Log.d("epf","true")
            }
            if(choix_window_radiobutton==window_manuelle_radiobutton.id){
                choix_window_detail=false
                Log.d("epf","false")
            }
            var  new_luminosite_consigne =luminosite_consigne_detail_edittext.text.toString()

            change_object(type,null,null,null,choix_window_detail,new_luminosite_consigne.toInt())
            finish()
        }


    }

    fun change_object(type:String?, humidite_plant_consigne_new:Int?, nombre_arrosage_new:Int?, volume_eau_journalier_new:Int?, automatique_new:Boolean?,luminosite_consigne_new:Int?){
        var type_Object=(
        when (type) {
            "HEATER" -> Type.HEATER
            "WINDOW" -> Type.WINDOW
            "LIGHT" ->Type.LIGHT
            "PLANT" -> Type.PLANT
            else -> Type.PLANT
        })

        changeobject = Object(object_id, object_name,
                type_Object,
                allumer_light,
                actif_volet,
                temp_consigne,
                temp_reel,
                humidite_plant_reel,
                valeur_luminosite,
                ph,
                humidite_air,
                nombre_arrosage_new,
                volume_eau_journalier_new,
                concentration_co2,
                concentration_lgp,
                concentration_fumee,
                humidite_plant_consigne_new,
                automatique_new,
            luminosite_consigne_new)
        runBlocking { objectDao.changeByName(changeobject) }

        var valeur_post_object  = "{\"valeur_humidite_consigne\":${humidite_plant_consigne_new},\"valeur_nombre_arrosage\":${nombre_arrosage_new},\"valeur_volume_eau_journalier\":${volume_eau_journalier_new},\"valeur_automatique\":${automatique_new},\"id\":${object_id},\"type\":\"${type}\",\"valeur_luminosite_consigne\":${luminosite_consigne_new},\"valeur_plant_mode\":false,\"valeur_window_mode\":false}"
        post_object(valeur_post_object)

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
    fun post_object(valeur_post_object:String) {

       ConnexionBaseMongoDb()
runBlocking {
            val requestBody = valeur_post_object.toRequestBody("application/json".toMediaTypeOrNull())
            service.postSetpoint(requestBody)}
        }

    }

