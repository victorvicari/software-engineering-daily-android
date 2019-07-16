package com.koalatea.sedaily.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.koalatea.sedaily.database.model.Comment

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment")
    suspend fun getAllComments(): List<Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episodes: List<Comment>)

    @Query("UPDATE comment SET upvoted = :newState, score = :newScore WHERE _id = :id")
    suspend fun vote(id: String, newState: Boolean, newScore: Int)

    @Query("DELETE FROM episode")
    fun clearTable()
}