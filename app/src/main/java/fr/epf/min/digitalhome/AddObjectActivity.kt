package fr.epf.min.digitalhome

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_object.*

class AddObjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)


        object_add_button.setOnClickListener{
            var name =object_name_edittext.text.toString()

            var object_id= object_type_RadioGroup.checkedRadioButtonId as Int

            var type =""
            if(object_id == object_heater_radiobutton.id ){
                type="HEATER"}
            if(object_id == object_light_radiobutton.id){
                type="LIGHT"}
            if(object_id == object_plant_radiobutton.id){
                type="PLANT"}
            if(object_id == object_window_radiobutton.id){
                type="WINDOW"}

            Log.d("test","Nom:${name}")

            Log.d("test","Type:${type}")

            finish()


        }
}
}