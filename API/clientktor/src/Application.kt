package co.devhack

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking


fun main() {
    val client = HttpClient(Apache) {
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
        }

        install(JsonFeature) {
            serializer = JacksonSerializer()
        }

        HttpResponseValidator {
            validateResponse { response ->
                when (response.status.value) {
                    in 300..399 -> throw RedirectResponseException(response)
                    in 400..499 -> throw ClientRequestException(response)
                    in 500..599 -> throw ServerResponseException(response)
                }
            }
        }
    }

    runBlocking {
        val post = Post(
            "ktor client 2",
            "Post from ktor client",
            "15/09/2020"
        )

        val json = jacksonObjectMapper()
        val successful = client.post<Successful>(URI_POST) {
            body = TextContent(json.writeValueAsString(post), contentType = ContentType.Application.Json)
        }

        println("Post Response -> ${successful.successful}")

        val posts = client.get<List<Post>?>(URI_POST)
        posts?.let {
            it.forEach { post ->
                println(post)
            }
        }
    }
}

const val URI_POST = "https://postsapi-zhh6smsl4q-uc.a.run.app/posts"

data class Post(
    val title: String,
    val description: String,
    val date: String
)

data class Successful(
    val successful: Boolean
)
