package fr.epf.min.digitalhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.CoroutinesRoom
import androidx.room.Room
import fr.epf.min.digitalhome.data.ObjectDao
import fr.epf.min.digitalhome.data.ObjectDataBase
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.list_object_heater_view.view.*
import kotlinx.android.synthetic.main.list_object_light_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_imageview
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_name_textview
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext


class ListObjectAdapter(val objects: List<Object>,val context: Context) : RecyclerView.Adapter<ListObjectAdapter.ObjectViewHolder>() {
    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    class ObjectViewHolder(val objectView: View) : RecyclerView.ViewHolder(objectView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        Dao()
        val `object`: Object = objects[viewType]
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = layoutInflater.inflate(when (`object`.type) {
            Type.HEATER -> R.layout.list_object_heater_view
            Type.LIGHT -> R.layout.list_object_light_view
            Type.PLANT -> R.layout.list_object_plant_view
            Type.WINDOW -> R.layout.list_object_window_view
        }, parent, false)



        return ObjectViewHolder(view)
    }


    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        Dao()
        val `object`: Object = objects[position]
        holder.objectView.object_name_textview.text =
                "${`object`.name}"
        holder.objectView.object_imageview.setImageResource(
                when (`object`.type) {
                    Type.HEATER -> R.drawable.heater
                    Type.LIGHT -> R.drawable.lightbulb
                    Type.PLANT -> R.drawable.plant
                    Type.WINDOW -> R.drawable.window
                })
        holder.objectView.setOnClickListener {
            with(it.context) {
                val `object_name` = `object`.name
                val intent = Intent(this, DetailsPlantActivity::class.java)
                intent.putExtra("object", `object_name`);
                startActivity(intent)


            }

        }
        heater(holder, `object`)
        light(holder, `object`)
        plant(holder,`object`)
    }

    override fun getItemCount() = objects.size


    fun heater(holder: ObjectViewHolder?, `object`: Object) {
        holder?.objectView?.Temp_consigne_textView?.text = `object`?.temp_consigne.toString()
        holder?.objectView?.plus_heater_button?.setOnClickListener {
            with(it.context) {
                Log.d("epf", "1")
                var temp_consigne=`object`.temp_consigne
                if (temp_consigne != null) {
                    temp_consigne += 1
                }
        var changeobject =  Object(`object`.id, "${`object`.name}",`object`.type,
                    null,
                    null,
                    null,
                    temp_consigne,
                    24, 50)
                runBlocking { objectDao.changeByName(changeobject)}
                holder?.objectView?.Temp_consigne_textView?.text = `object`?.temp_consigne.toString()

            }
        }
        holder?.objectView?.less_heater_button?.setOnClickListener {
            with(it.context) {
                var temp_consigne=`object`.temp_consigne
                if (temp_consigne != null) {
                    temp_consigne= temp_consigne!! -1
                }
                var changeobject =  Object(`object`.id, "${`object`.name}",`object`.type,
                        null,
                        null,
                        null,
                        temp_consigne,
                        24, 50)
                runBlocking { objectDao.changeByName(changeobject)}
                holder?.objectView?.Temp_consigne_textView?.text = `object`?.temp_consigne.toString()

            }
        }


    }

    fun light(holder: ObjectViewHolder?, `object`: Object?) {
        holder?.objectView?.lamp_allumer_switch?.setOnClickListener {
            with(it.context) {
                val allumer = holder?.objectView?.lamp_allumer_switch.isChecked
                if(allumer){
                    Log.d("epf", "true")}
                if(!allumer){
                    Log.d("epf", "false")}


            }
        }
    }

    fun plant(holder:ObjectViewHolder?,`object`: Object?){

        holder?.objectView?.Pourcentage_eau_plant_TextView?.text = `object`?.pourcentage_eau_plant.toString() + "%"

    }

    fun window(holder:ObjectViewHolder?,position: Int?){}

    fun Dao(){
        //accés a la


        database = Room.databaseBuilder(
                context, ObjectDataBase::class.java, "clients-db"

        ).build()


        objectDao = database.getObjectDao()
    }
}
