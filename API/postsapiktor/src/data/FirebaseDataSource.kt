package co.devhack.data

import co.devhack.data.entities.PostEntity
import com.google.cloud.firestore.Firestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSource(
    private val db: Firestore
) {
    companion object {
        const val POSTS_COLLECTION = "posts"
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val DATE_FIELD = "date"
    }

    suspend fun addPost(post: PostEntity): Boolean =
        suspendCoroutine { continuation ->
            try {
                db.collection(POSTS_COLLECTION)
                    .add(post)
                    .get()
                continuation.resume(true)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }

    suspend fun getAllPosts(): List<PostEntity> =
        suspendCoroutine { continuation ->
            try {
                val querySnap = db.collection(POSTS_COLLECTION)
                    .get()
                    .get()

                val posts = mutableListOf<PostEntity>()
                querySnap.documents.forEach { document ->
                    posts.add(
                        PostEntity(
                            document.getString(TITLE_FIELD) ?: "",
                            document.getString(DESCRIPTION_FIELD) ?: "",
                            document.getString(DATE_FIELD) ?: ""
                        )
                    )
                }

                continuation.resume(posts)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
}
