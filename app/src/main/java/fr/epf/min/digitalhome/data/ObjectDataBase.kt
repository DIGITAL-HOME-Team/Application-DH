package fr.epf.min.digitalhome.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.epf.min.digitalhome.model.Object
import fr.epf.min.digitalhome.model.Type


@Database(entities = [Object::class], version = 1)
@TypeConverters(TypeConversion::class)
abstract class ObjectDataBase: RoomDatabase(){

    abstract fun getObjectDao() : ObjectDao

}

class TypeConversion{

    @TypeConverter fun toType(value: String) = Type.valueOf(value)
    @TypeConverter fun toSting(value: Type)= value.name



}
