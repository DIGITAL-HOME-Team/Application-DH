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
import fr.epf.min.digitalhome.model.Object
import kotlinx.android.synthetic.main.activity_details_plant.*
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates


class DetailsPlantActivity : AppCompatActivity() {

    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    var `object_id` by Delegates.notNull<Int>()
    lateinit var `object_name`: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_plant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent

        if (intent.hasExtra("object_id")) {
            `object_id` = intent?.getIntExtra("object_id",1)!!
            `object_name` = intent.getStringExtra("object_name").toString()
        }

        Detail_plant_textview?.text= "${`object_name`}"
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

                            Dao()
                            runBlocking {
                                val objects = objectDao.findByid(`object_id`)
                                objectDao.deleteObject(objects) }
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
            this, ObjectDataBase::class.java, "clients-db"

        ).build()
        objectDao = database.getObjectDao()
    }
}