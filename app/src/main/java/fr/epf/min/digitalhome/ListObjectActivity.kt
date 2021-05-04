package fr.epf.min.digitalhome

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_object.*


class ListObjectActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        var type : String = ""
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type").toString()
        }
        Log.d("test","Nom:${type}")
        val types=(
        when(type){
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

        val `object1` = Object("salut",types)
        val `object2` = Object("salut",types)
        val `object` = listOf(`object1`,`object2`)

        list_objects_recyclerview.adapter= ListObjectAdapter(`object`)

    }
}











