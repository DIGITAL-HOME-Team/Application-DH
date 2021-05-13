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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AddObjectActivity : AppCompatActivity() {


    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)


        object_add_button.setOnClickListener{
            var name =object_name_edittext.text.toString()

            var object_id= object_type_RadioGroup.checkedRadioButtonId as Int

            var type :Type=(
            when (object_id) {
                object_heater_radiobutton.id  -> Type.HEATER
                object_window_radiobutton.id -> Type.WINDOW
                object_light_radiobutton.id-> Type.LIGHT
                object_plant_radiobutton.id -> Type.PLANT
                else -> Type.PLANT
            })

            Dao()

            val `object` = Object(null, "${name}",type,
                    null,
                    null,
                    null,
                    24,
                    24, 50)
            runBlocking { objectDao.addObject(`object`) }

            finish()


        }
}


    private fun Dao(){
        //accés a la


        database = Room.databaseBuilder(
            this, ObjectDataBase::class.java, "clients-db"

        ).build()


        objectDao = database.getObjectDao()
    }



}