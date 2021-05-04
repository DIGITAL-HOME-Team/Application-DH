package fr.epf.min.digitalhome.model

enum class Type{

    LIGHT,WINDOW,PLANT,HEATER

}


data class Object(
        val name:String,
        val type: Type

){}


/**{
    companion object {
        fun all() =(1..30).map {
            Object(
                    "name${it}",
                    if(it%2==0) Type.LIGHT else Type.HEATER
            )
        }


    }
}**/