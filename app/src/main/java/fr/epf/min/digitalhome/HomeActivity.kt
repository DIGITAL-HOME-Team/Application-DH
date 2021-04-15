package fr.epf.min.digitalhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    list_plant_button.setOnClickListener{
        val intent = Intent(this, ListPlantActivity::class.java)
        startActivity(intent)
    }

}
