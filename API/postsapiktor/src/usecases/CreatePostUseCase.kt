package co.devhack.usecases

import co.devhack.common.Either
import co.devhack.common.Failure
import co.devhack.usecases.domain.Post
import co.devhack.usecases.repositories.PostRepository

class CreatePostUseCase(
    private val postRepository: PostRepository
) : UseCaseBase<Boolean, CreatePostUseCase.Params>() {

    data class Params(val post: Post)

    override suspend fun run(params: Params): Either<Failure, Boolean> =
        postRepository.createPost(params.post)
}
