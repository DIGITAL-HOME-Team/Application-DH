package fr.epf.min.digitalhome

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type
import kotlinx.android.synthetic.main.list_object_view.view.*

class ListObjectAdapter(val objects: List<Object>) : RecyclerView.Adapter<ListObjectAdapter.ObjectViewHolder>() {
    class ObjectViewHolder(val objectView: View) : RecyclerView.ViewHolder(objectView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        val layoutInflater : LayoutInflater= LayoutInflater.from(parent.context)
        val view : View = layoutInflater.inflate(R.layout.list_object_view,parent,false)

        return ObjectViewHolder(view)
    }



    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        val `object`: Object =objects[position]
        holder.objectView.object_name_textview.text=
                "${`object`.name}"
        holder.objectView.object_imageview.setImageResource(
                when(`object`.type){
                    Type.HEATER -> R.drawable.heater
                    Type.LIGHT -> R.drawable.lightbulb
                    Type.PLANT -> R.drawable.plant
                    Type.WINDOW -> R.drawable.window
                })
        holder.objectView.setOnClickListener{
            with(it.context){
                val intent= Intent(this,DetailsPlantActivity::class.java)
                intent.putExtra("id",position)
                startActivity(intent)

            }

        }
    }

    override fun getItemCount() = objects.size
}
