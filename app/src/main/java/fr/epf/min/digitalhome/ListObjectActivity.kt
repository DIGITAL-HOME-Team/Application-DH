package fr.epf.min.digitalhome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.min.digitalhome.data.*
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_object.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory



class ListObjectActivity  : AppCompatActivity() {

    lateinit var objects: List<Object>
    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    lateinit var type: String
    lateinit var service: ObjectService
    lateinit var result: GetObjectsByTypeResult

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent


            type = intent?.getStringExtra("type").toString()

        val types = (
                when (type) {
                    "HEATER" -> Type.HEATER
                    "WINDOW" -> Type.WINDOW
                    "LIGHT" -> Type.LIGHT
                    "PLANT" -> Type.PLANT
                    else -> Type.PLANT
                })

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)

        list_objects_recyclerview.layoutManager =
                LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_object,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_object_action -> {
                val intent = Intent(this, AddObjectActivity::class.java)
                startActivity(intent)

            }
            R.id.refresh_list_action ->{
                //synchro()
            }
        }
        return super.onOptionsItemSelected(item)
    }

fun ConnexionBaseMongoDb(){
    runBlocking {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.215.196:5000/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        service = retrofit.create(ObjectService::class.java)
    }

}
    //fun synchro() {
//
  //      runBlocking {
    //    ConnexionBaseMongoDb()
       //     result = service.getObjects("test")
         //   Log.d("EPF","$result")
           // val users= result.results
            //users.map{
              //  Object(null,it.name,
                //        when(it.type){
                  //     "PLANT" -> Type.PLANT
                    //        "HEATER" -> Type.HEATER
                      //  "WINDOW" -> Type.WINDOW
                      //  "LIGHT"-> Type.LIGHT
                        //    else -> Type.LIGHT
                       // },
                        //null,
                       // null,
                        //null,
                       // null,
                       // 50,null)
          //  }.map{
                //objects.add(it)
            //    objectDao.addObject(it)
           // }
      //  }
      //  list_objects_recyclerview.adapter?.notifyDataSetChanged()
   // }

    override fun onStart() {

        super.onStart()
        Dao()


        runBlocking {
            try {
            ConnexionBaseMongoDb()
                Log.d("epf","test")
               result=service.getObjectsByType("listbytype",type)
            val objects= result.results
            objects.map{
                Object(it.id,it.name,
                       it.type,
                        it.allumer_light,
                        it.actif_volet,
                        it.temp_consigne,
                        it.temp_reel,
                        it.pourcentage_eau_plant,it.valeur_luminosite)

            }.map{

                 objectDao.addObject(it)}




            }catch (e:Exception){
                Log.d("epf", e.toString())
           objects = objectDao.getAllObjectsOfType(type).toMutableList()}

            list_objects_recyclerview.adapter = ListObjectAdapter(objects,this@ListObjectActivity)
        }

    }

    private fun Dao(){
        //accés a la base
        database = Room.databaseBuilder(
            this, ObjectDataBase::class.java, "objects-db"

        ).build()
        objectDao = database.getObjectDao()
    }

    }











