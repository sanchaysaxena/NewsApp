package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//we already had this data class created using plugin we just added @entity and id and defined it as primary key
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
):Serializable{
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }
}
//we add above override func to remove error
// java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.hashCode()' on a null object reference


//here source data type is not supported by the database to store values
//so we use type convertors->these are used to convert complex data types thaat are not supported by DB to a format that
//is supported by database and vice versa
//to do this we serialize this,serialization->process of converting an object into format that can be easily stored or transmitted
