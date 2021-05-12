package fr.epf.min.digitalhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        //this.deleteDatabase ("clients-db") //Ne pas oublier de supprimer

    list_plant_button.setOnClickListener{
        val type :String = "PLANT"

        val intent = Intent(this, ListObjectActivity::class.java)
        intent.putExtra("type", type);
        startActivity(intent)
    }
    list_light_button.setOnClickListener {
        val type :String = "LIGHT"
        val intent = Intent(this, ListObjectActivity::class.java)
        intent.putExtra("type", type);
        startActivity(intent)
    }
    list_window_button.setOnClickListener {
        val type :String = "WINDOW"
        val intent = Intent(this, ListObjectActivity::class.java)
        intent.putExtra("type", type);
        startActivity(intent)
    }
    list_heater_button.setOnClickListener {
        val type :String = "HEATER"
        val intent = Intent(this, ListObjectActivity::class.java)
        intent.putExtra("type", type);
        startActivity(intent)
    }

    }
}
