package co.devhack.endpoints

import co.devhack.common.Failure
import co.devhack.endpoints.dto.DTOError
import co.devhack.endpoints.dto.DTOSuccessful
import co.devhack.usecases.CreatePostUseCase
import co.devhack.usecases.GetAllPostUseCase
import co.devhack.usecases.UseCaseBase
import co.devhack.usecases.domain.Post
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PostController {

    companion object {
        const val URI_POST = "/posts"
        const val CODE_SERVER_ERROR = "500"
        const val CODE_BAD_REQUEST_ERROR = "400"
        const val MESSAGE_BAD_REQUEST_ERROR = "Bad Request"
    }

    private val logger: Logger = LoggerFactory.getLogger(PostController::class.java)

    fun Route.postsRouting(
        createPostUseCase: CreatePostUseCase,
        getAllPostUseCase: GetAllPostUseCase
    ) {

        route(URI_POST) {

            get {
                getAllPostUseCase.execute(
                    UseCaseBase.None()
                ) {
                    it.either({ failure ->
                        handlerError(failure, call)
                    }, { posts ->
                        call.respond(
                            HttpStatusCode.OK,
                            posts
                        )
                    })
                }
            }

            post {
                try {
                    val post = call.receiveOrNull<Post>()

                    post?.let { postParam ->
                        createPostUseCase.execute(
                            CreatePostUseCase.Params(postParam)
                        ) {
                            it.either(
                                { failure ->
                                    handlerError(failure, call)
                                },
                                { resp ->
                                    call.respond(
                                        HttpStatusCode.OK,
                                        DTOSuccessful(
                                            resp
                                        )
                                    )
                                }
                            )
                        }
                    } ?: handlerError(Failure.BadRequest(MESSAGE_BAD_REQUEST_ERROR), call)

                } catch (e: Exception) {
                    handlerError(Failure.ServerError(e), call)
                }
            }
        }
    }

    private suspend fun handlerError(failure: Failure, call: ApplicationCall) {
        when (failure) {
            is Failure.ServerError -> {
                logger.error(failure.ex.message)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    DTOError(
                        CODE_SERVER_ERROR,
                        failure.ex.message ?: ""
                    )
                )
            }
            is Failure.BadRequest -> {
                logger.error(failure.message)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    DTOError(
                        CODE_BAD_REQUEST_ERROR,
                        failure.message
                    )
                )
            }
        }
    }
}
