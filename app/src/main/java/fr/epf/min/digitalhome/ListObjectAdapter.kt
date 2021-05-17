package fr.epf.min.digitalhome

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fr.epf.min.digitalhome.data.ObjectDao
import fr.epf.min.digitalhome.data.ObjectDataBase
import fr.epf.min.digitalhome.data.ObjectService
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.list_object_heater_view.view.*
import kotlinx.android.synthetic.main.list_object_light_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_imageview
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_name_textview
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ListObjectAdapter(val objects: List<Object>,val context: Context) : RecyclerView.Adapter<ListObjectAdapter.ObjectViewHolder>() {
    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    lateinit var valeur_post_object: String
    lateinit var uri:String
    lateinit var changeobject:Object
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
                val type = (
                        when (`object`.type) {
                            Type.HEATER -> "HEATER"
                                Type.WINDOW   -> "WINDOW"
                                Type.LIGHT -> "LIGHT"
                                Type.PLANT-> "PLANT"
                        })
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("object_id", `object`.id);
                intent.putExtra("object_name", `object`.name);
                intent.putExtra("pourcentage_eau_plant",`object`.pourcentage_eau_plant)
                intent.putExtra("actif_volet",`object`.actif_volet)
                intent.putExtra("temp_consigne",`object`.temp_consigne)
                intent.putExtra("temp_reel",`object`.temp_reel)
                intent.putExtra("allumer_light",`object`.allumer_light)
                intent.putExtra("type", type);
                startActivity(intent)


            }

        }
        heater(holder, `object`)
        light(holder, `object`)
        plant(holder, `object`)
    }

    override fun getItemCount() = objects.size


    fun heater(holder: ObjectViewHolder?, `object`: Object) {

        uri="heater"
        try{
       runBlocking {   changeobject =objectDao.findByid(`object`.id) }


        holder?.objectView?.Temp_consigne_textView?.text = `object`?.temp_consigne.toString()+ "°C"
        holder?.objectView?.plus_heater_button?.setOnClickListener {
            with(it.context) {

                var temp_consigne =  changeobject.temp_consigne
                if (temp_consigne != null) {
                    temp_consigne += 1
                }
                 changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                        null,
                        null,
                         temp_consigne,
                         24,
                         50,null)
                runBlocking { objectDao.changeByName(changeobject) }
                valeur_post_object  = "{\"valeur_heater\":\"${temp_consigne}\"}"
                post_object(valeur_post_object)
                holder?.objectView?.Temp_consigne_textView?.text = changeobject?.temp_consigne.toString()+ "°C"


            }

        }

        holder?.objectView?.less_heater_button?.setOnClickListener {
            with(it.context) {
                var temp_consigne =  changeobject.temp_consigne
                if (temp_consigne != null) {
                    temp_consigne -= 1
                }
                changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                        null,
                        null,
                        temp_consigne,
                        24,
                        50,null)
                runBlocking { objectDao.changeByName(changeobject) }
                valeur_post_object  = "{\"valeur_heater\":\"${temp_consigne}\"}"
                post_object(valeur_post_object)
                holder?.objectView?.Temp_consigne_textView?.text = changeobject?.temp_consigne.toString() + "°C"

            }
        }

        }
        catch(e:Exception){

        }
    }

    fun light(holder: ObjectViewHolder?, `object`: Object?) {

        holder?.objectView?.lamp_allumer_switch?.setOnClickListener {
            with(it.context) {
                val allumer = holder?.objectView?.lamp_allumer_switch.isChecked
                if (allumer) {
                     valeur_post_object  = "{\"valeur_light\":\"true\"}"
                }

                if (!allumer) {
                    valeur_post_object  = "{\"valeur_light\":\"false\"}"
                }
                post_object(valeur_post_object)


            }
        }
    }

    fun plant(holder: ObjectViewHolder?, `object`: Object?) {

        holder?.objectView?.Pourcentage_eau_plant_TextView?.text = `object`?.pourcentage_eau_plant.toString() + "%"

    }

    fun window(holder: ObjectViewHolder?, position: Int?) {

    }

    fun Dao() {
        //accés a la


        database = Room.databaseBuilder(
                context, ObjectDataBase::class.java, "objects-db"

        ).build()


        objectDao = database.getObjectDao()
    }

    fun post_object(valeur_post_object:String) {

        runBlocking {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.215.196:5000/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            val service = retrofit.create(ObjectService::class.java)

            val requestBody = valeur_post_object.toRequestBody("application/json".toMediaTypeOrNull())
            service.postObjects(requestBody)
        }

    }
}
