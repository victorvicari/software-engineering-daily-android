package com.koalatea.sedaily.database.converter

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.koalatea.sedaily.database.model.Author
import com.koalatea.sedaily.database.model.Comment

class CommentConverter {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun authorCommentsToJsonString(author: Author?): String? {
        return author?.let { gson.toJson(author) }
    }

    @TypeConverter
    fun jsonStringToAuthorComments(json: String?): Author? {
        return json?.let { gson.fromJson(json, Author::class.java) }
    }

    @TypeConverter
    fun mentionsCommentsToJsonString(mentions: List<String>?): String? {
        return mentions?.let { gson.toJson(mentions) }
    }

    @TypeConverter
    fun jsonStringToMentionsComments(json: String?): List<String>? {
        return json?.let { gson.fromJson(json, object : TypeToken<ArrayList<List<String>>>() {}.type) }
    }

    @TypeConverter
    fun repliedCommentsToJsonString(replied: List<Comment>?): String? {
        return replied?.let { gson.toJson(replied) }
    }

    @TypeConverter
    fun jsonStringToRepliedComments(json: String?): List<Comment>? {
        return json?.let { gson.fromJson(json, object : TypeToken<ArrayList<Comment>>() {}.type) }
    }

}