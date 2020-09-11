package co.devhack.usecases.repositories

import co.devhack.common.Either
import co.devhack.common.Failure
import co.devhack.usecases.domain.Post

interface PostRepository {
    suspend fun createPost(post: Post): Either<Failure, Boolean>
    suspend fun getAllPost(): Either<Failure, List<Post>>
}
