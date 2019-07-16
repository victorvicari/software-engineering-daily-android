package com.koalatea.sedaily.repository

import androidx.annotation.MainThread
import com.koalatea.sedaily.database.AppDatabase
import com.koalatea.sedaily.database.model.Comment
import com.koalatea.sedaily.network.NetworkManager
import com.koalatea.sedaily.network.Resource
import com.koalatea.sedaily.network.SEDailyApi
import com.koalatea.sedaily.network.toException
import com.koalatea.sedaily.util.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentsRepository(
        private val api: SEDailyApi,
        private val db: AppDatabase,
        private val networkManager: NetworkManager,
        private val sessionRepository: SessionRepository
) {

    suspend fun fetchComments(entityId: String) = withContext(Dispatchers.IO) {
        val response = safeApiCall { api.getEpisodeCommentsAsync(entityId).await() }
        val comments = response?.body()
        if (response?.isSuccessful == true && comments != null) {
            insertResultInDb(comments.result)
            Resource.Success(db.commentsDao().getAllComments())
        } else {
            Resource.Error(response?.errorBody().toException(), networkManager.isConnected)
        }
    }

    suspend fun addComment(entityId: String, parentCommentId: String? = null, commentContent: String) = withContext(Dispatchers.IO) {
        if (sessionRepository.isLoggedIn) {
            val response = safeApiCall { api.addEpisodeCommentAsync(entityId, parentCommentId, commentContent).await() }
            val addCommentResponse = response?.body()
            if (response?.isSuccessful == true && addCommentResponse != null) {
                Resource.Success(true)
            } else {
                Resource.Error(response?.errorBody().toException(), networkManager.isConnected)
            }
        } else {
            Resource.RequireLogin
        }
    }

    suspend fun vote(commentId: String, originalState: Boolean, originalScore: Int) = withContext(Dispatchers.IO) {
        if (sessionRepository.isLoggedIn) {
            val response = if (originalState) {
                db.commentsDao().vote(commentId, !originalState, originalScore + 1)

                safeApiCall { api.upvoteCommentAsync(commentId).await() }
            } else {
                safeApiCall { api.upvoteCommentAsync(commentId).await() }
            }

            if (response?.isSuccessful == false || response?.body() == null) {
                db.commentsDao().vote(commentId, originalState, originalScore)

                return@withContext false
            }

            return@withContext true
        } else {
            Resource.RequireLogin
        }
    }

    @MainThread
    private fun insertResultInDb(comments: List<Comment>?) {
        GlobalScope.launch {
            comments?.let { comments -> db.commentsDao().insert(comments) }
        }
    }

}