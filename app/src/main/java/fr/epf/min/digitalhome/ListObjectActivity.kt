package fr.epf.min.digitalhome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.activity_object.*
import java.sql.*
import java.sql.DriverManager.getConnection
import java.util.*


class ListObjectActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        var type: String = ""
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type").toString()
        }
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

        val `object1` = Object("salut", types)
        val `object2` = Object("salut", types)
        val `object` = listOf(`object1`, `object2`)

        list_objects_recyclerview.adapter = ListObjectAdapter(`object`)

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
        }
        return super.onOptionsItemSelected(item)
    }




/*
    internal var conn: Connection? = null

    fun main(args: Array<String>) {
        // make a connection to MySQL Server
        getConnection()
        // execute the query via connection object
        executeMySQLQuery()
    }
    fun executeMySQLQuery() {
        var stmt: Statement? = null
        var resultset: ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")

        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        }
    }
        fun getConnection() {
            val connectionProps = Properties()
            //connectionProps.put("host", "195.144.11.150")
            connectionProps.put("user", "zdj62853")
            connectionProps.put("password", "DH1904!!")
            connectionProps.put("database", "zdj62853")
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val conn = DriverManager.getConnection("195.144.11.150", connectionProps)
            } catch (ex: SQLException) {
                ex.printStackTrace()// handle any errors
            } catch (ex: Exception) {
                ex.printStackTrace() // handle any errors
            }
        }
*/
    }











