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
import kotlinx.android.synthetic.main.list_object_window_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.*
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_imageview
import kotlinx.android.synthetic.main.list_object_plant_view.view.object_name_textview
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.properties.Delegates


class ListObjectAdapter(val objects: List<Object>,val context: Context) : RecyclerView.Adapter<ListObjectAdapter.ObjectViewHolder>() {
    lateinit var database: ObjectDataBase
    lateinit var objectDao: ObjectDao
    lateinit var valeur_post_object: String
    lateinit var uri: String
    lateinit var changeobject: Object
    var pressed_top by Delegates.notNull<Int>()
    var pressed_top_plant by Delegates.notNull<Int>()
    var pressed_bottom by Delegates.notNull<Int>()
    var ip = "http://192.168.137.222:5000/"

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
        pressed_top=0
        pressed_top_plant=0
        pressed_bottom=0



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
                            Type.WINDOW -> "WINDOW"
                            Type.LIGHT -> "LIGHT"
                            Type.PLANT -> "PLANT"
                        })
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("object_id", `object`.id);
                intent.putExtra("object_name", `object`.name);
                intent.putExtra("valeur_luminosite", `object`.valeur_luminosite)
                intent.putExtra("pourcentage_eau_plant", `object`.humidite_plant_reel)
                intent.putExtra("actif_volet", `object`.actif_volet)
                intent.putExtra("temp_consigne", `object`.temp_consigne)
                intent.putExtra("temp_reel", `object`.temp_reel)
                intent.putExtra("allumer_light", `object`.allumer_light)
                intent.putExtra("ph", `object`.ph)
                intent.putExtra("humidite_air", `object`.humidite_air)
                intent.putExtra("nombre_arrosage", `object`.nombre_arrosage)
                intent.putExtra("volume_eau_journalier", `object`.volume_eau_journalier)
                intent.putExtra("concentration_co2", `object`.concentration_co2)
                intent.putExtra("concentration_lgp", `object`.concentration_lgp)
                intent.putExtra("concentration_fumee", `object`.concentration_fumee)
                intent.putExtra("humidite_plant_consigne", `object`.humidite_plant_consigne)
                intent.putExtra("humidite_plant_reel", `object`.humidite_plant_reel)
                intent.putExtra("type", type)
                intent.putExtra("automatique", `object`.automatique)
                intent.putExtra("luminosite_consigne", `object`.luminosite_consgine)
                startActivity(intent)

            }

        }
        val type = (
                when (`object`.type) {
                    Type.HEATER -> "HEATER"
                    Type.WINDOW -> "WINDOW"
                    Type.LIGHT -> "LIGHT"
                    Type.PLANT -> "PLANT"
                })
        heater(holder, `object`, type)
        light(holder, `object`, type)
        plant(holder, `object`,type)
        window(holder, `object`, type)
    }

    override fun getItemCount() = objects.size


    fun heater(holder: ObjectViewHolder?, `object`: Object, type: String) {

        uri = "heater"
        try {
            runBlocking { changeobject = objectDao.findByid(`object`.id) }


            holder?.objectView?.Temp_consigne_textView?.text = `object`?.temp_consigne.toString() + "°C"
            holder?.objectView?.plus_heater_button?.setOnClickListener {
                with(it.context) {

                    var temp_consigne = changeobject.temp_consigne
                    if (temp_consigne != null) {
                        temp_consigne += 1
                    }
                    changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                            null,
                            null,
                            temp_consigne,
                            `object`.temp_reel,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null)
                    runBlocking { objectDao.changeByName(changeobject) }
                    valeur_post_object = "{\"valeur_heater\":\"${temp_consigne}\",\"id\":${`object`.id},\"type\":\"${type}\"}"
                    post_object(valeur_post_object)
                    holder?.objectView?.Temp_consigne_textView?.text = changeobject?.temp_consigne.toString() + "°C"


                }

            }

            holder?.objectView?.less_heater_button?.setOnClickListener {
                with(it.context) {
                    var temp_consigne = changeobject.temp_consigne
                    if (temp_consigne != null) {
                        temp_consigne -= 1
                    }
                    changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                            null,
                            null,
                            temp_consigne,
                            `object`.temp_reel,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null)
                    runBlocking { objectDao.changeByName(changeobject) }
                    valeur_post_object = "{\"valeur_heater\":\"${temp_consigne}\",\"id\":${`object`.id},\"type\":\"${type}\"}"
                    post_object(valeur_post_object)
                    holder?.objectView?.Temp_consigne_textView?.text = changeobject?.temp_consigne.toString() + "°C"

                }
            }

        } catch (e: Exception) {

        }
    }

    fun light(holder: ObjectViewHolder?, `object`: Object?, type: String) {

        holder?.objectView?.lamp_allumer_switch?.setOnClickListener {
            with(it.context) {

                val allumer = holder?.objectView?.lamp_allumer_switch.isChecked

                if (allumer) {
                    if (`object` != null) {
                        changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                                allumer,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null)
                        runBlocking { objectDao.changeByName(changeobject) }
                        valeur_post_object = "{\"valeur_light\":\"true\",\"id\":${`object`.id},\"type\":\"${type}\"}"
                    }
                }

                if (!allumer) {
                    if (`object` != null) {
                        changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                                allumer,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null)
                        runBlocking { objectDao.changeByName(changeobject) }
                        valeur_post_object = "{\"valeur_light\":\"false\",\"id\":${`object`.id},\"type\":\"${type}\"}"
                    }
                }
                post_object(valeur_post_object)


            }
        }
    }

    fun plant(holder: ObjectViewHolder?, `object`: Object?,type:String) {

        holder?.objectView?.Pourcentage_eau_plant_TextView?.text = `object`?.humidite_plant_reel.toString() + "%"
        holder?.objectView?.actif_pompe_ImageButton?.setOnClickListener {
            with(it.context) {
                // this goes wherever you setup your button listener:
                pressed_top_plant = pressed_top_plant + 1
                var plant_actif = 0

                if (pressed_top_plant == 2) {
                    plant_actif = 0
                    pressed_top_plant = 0
                } else {
                    plant_actif = 1

                }
                valeur_post_object = "{\"valeur_plant_actif\":1,\"id\":${`object`?.id},\"type\":\"${type}\",\"valeur_plant_mode\":true}"
                post_object(valeur_post_object)
            }
        }
    }

    fun window(holder: ObjectViewHolder?, `object`: Object, type: String) {

        // this goes somewhere in your class:

        holder?.objectView?.top_window_button?.setOnClickListener {
            with(it.context) {
                var automatique_volet=false
                // this goes wherever you setup your button listener:
                pressed_top=pressed_top+1
                var actif_volet=0
                if(pressed_top==2) {
                    actif_volet = 0
                    pressed_top=0
                }else{
                    actif_volet=1

                }

                changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                        null,
                        actif_volet,
                        null,
                        `object`.temp_reel,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    automatique_volet,
                        null)
                runBlocking { objectDao.changeByName(changeobject) }
                valeur_post_object = "{\"valeur_window\":${actif_volet},\"id\":${`object`.id},\"type\":\"${type}\",\"valeur_automatique\":${automatique_volet},\"valeur_window_mode\":true}"
                post_object(valeur_post_object)

            }

        }
      holder?.objectView?.bottom_window_button?.setOnClickListener {
            with(it.context) {
                // this goes wherever you setup your button listener:
                pressed_bottom=pressed_bottom+1
                var actif_volet=0
                var automatique_volet=false
                if(pressed_bottom==2) {
                    actif_volet = 0
                    pressed_bottom=0
                }else{
                    actif_volet=-1
                    }
                changeobject = Object(`object`.id, "${`object`.name}", `object`.type,
                        null,
                        actif_volet,
                        null,
                        `object`.temp_reel,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    automatique_volet,
                        null)
                runBlocking { objectDao.changeByName(changeobject) }
                valeur_post_object = "{\"valeur_window\":${actif_volet},\"id\":${`object`.id},\"type\":\"${type}\",\"valeur_automatique\":${automatique_volet},\"valeur_window_mode\":true}"
                post_object(valeur_post_object)

            }

        }

    }

        fun Dao() {
            //accés a la


            database = Room.databaseBuilder(
                    context, ObjectDataBase::class.java, "objects-db"

            ).build()


            objectDao = database.getObjectDao()
        }

        fun post_object(valeur_post_object: String) {

            runBlocking {
                val retrofit = Retrofit.Builder()
                        .baseUrl(ip)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()
                val service = retrofit.create(ObjectService::class.java)

                val requestBody = valeur_post_object.toRequestBody("application/json".toMediaTypeOrNull())
                service.postSetpoint(requestBody)
            }

        }


    }

