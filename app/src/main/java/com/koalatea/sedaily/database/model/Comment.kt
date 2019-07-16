package com.koalatea.sedaily.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koalatea.sedaily.util.toUTCDate
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Comment(
        val _id: String,
        val author: Author,
        val rootEntity: String,
        val content: String,
        val mentions: List<String>?,
        val deleted: Boolean?,
        val dateCreated: String,
        val score: Int?,
        val replies: List<Comment>?,
        val upvoted: Boolean?,
        val downvoted: Boolean?
) : Parcelable {

    @Suppress("SuspiciousVarProperty")
    @field:PrimaryKey
    var uniqueKey: String = _id
        get() = _id

    val utcDateCreated: Date?
        get() = dateCreated.toUTCDate()

}