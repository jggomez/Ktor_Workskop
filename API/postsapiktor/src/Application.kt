package co.devhack

import co.devhack.di.postRepositoryModule
import co.devhack.di.postsUseCaseModule
import co.devhack.endpoints.PostController
import co.devhack.usecases.CreatePostUseCase
import co.devhack.usecases.GetAllPostUseCase
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.posts() {

    install(Koin) {
        slf4jLogger()
        modules(
            listOf(
                postRepositoryModule,
                postsUseCaseModule
            )
        )
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Routing) {
        val createPostUseCase by inject<CreatePostUseCase>()
        val getAllPostUseCase by inject<GetAllPostUseCase>()
        PostController().apply {
            postsRouting(
                createPostUseCase,
                getAllPostUseCase
            )
        }
    }
}
