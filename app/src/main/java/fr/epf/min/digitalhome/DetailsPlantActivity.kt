package fr.epf.min.digitalhome

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import fr.epf.min.digitalhome.model.Object
import kotlinx.android.synthetic.main.activity_details_plant.*


class DetailsPlantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_plant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        var `object`: String = ""
        if (intent.hasExtra("object")) {
            `object` = intent.getStringExtra("object").toString()
        }

        Detail_plant_textview?.text= "${`object`}"
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
}