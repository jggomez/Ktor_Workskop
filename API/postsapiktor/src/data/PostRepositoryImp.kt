package co.devhack.data

import co.devhack.common.Either
import co.devhack.common.Failure
import co.devhack.data.entities.PostEntity
import co.devhack.usecases.domain.Post
import co.devhack.usecases.repositories.PostRepository

class PostRepositoryImp(
    private val firebaseDataSource: FirebaseDataSource
) : PostRepository {
    override suspend fun createPost(post: Post): Either<Failure, Boolean> =
        try {
            Either.Right(
                firebaseDataSource.addPost(
                    PostEntity(
                        post.title,
                        post.description,
                        post.date
                    )
                )
            )
        } catch (e: Exception) {
            Either.Left(Failure.ServerError(e))
        }

    override suspend fun getAllPost(): Either<Failure, List<Post>> =
        try {
            Either.Right(
                firebaseDataSource.getAllPosts().map {
                    it.toPost()
                }
            )
        } catch (e: Exception) {
            Either.Left(Failure.ServerError(e))
        }
}
