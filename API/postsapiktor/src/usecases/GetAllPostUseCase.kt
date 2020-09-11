package co.devhack.usecases

import co.devhack.common.Either
import co.devhack.common.Failure
import co.devhack.usecases.domain.Post
import co.devhack.usecases.repositories.PostRepository

class GetAllPostUseCase(
    private val postRepository: PostRepository
) : UseCaseBase<List<Post>, UseCaseBase.None>() {
    override suspend fun run(params: None): Either<Failure, List<Post>> =
        postRepository.getAllPost()
}
