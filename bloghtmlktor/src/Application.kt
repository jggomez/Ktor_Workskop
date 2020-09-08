package co.devhack

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.moduleHTML() {

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Sessions) {
        cookie<Posts>(SESSION)
    }

    routing {

        static(ROUTE_STATIC) {
            resources("static")
        }

        get(ROUTE_POSTS) {
            val session = call.sessions.get<Posts>()
            session?.let { posts ->
                call.respondHtml {
                    head {
                        title("POSTS")
                    }
                    body {
                        br { }
                        h2 { +"POSTS" }
                        ul {
                            posts.posts.split("|").forEach {
                                li { +it }
                            }
                        }
                    }
                }
            } ?: call.respond(HttpStatusCode.BadRequest, MESSAGE_BAD_REQUEST)
        }

        route(ROUTE_BLOG) {

            get {
                call.respond(FreeMarkerContent(INDEX_PAGE, null))
            }

            post {
                val parameters = call.receiveParameters()
                if (parameters[TITLE].validate()
                    && parameters[DESCRIPTION].validate()
                    && parameters[DATE].validate()
                ) {
                    val postBlog = PostBlog(
                        parameters[TITLE].toString(),
                        parameters[DESCRIPTION].toString(),
                        parameters[DATE].toString()
                    )
                    posts.add(postBlog)
                    log.info(postBlog.toString())
                    log.info(posts.toString())

                    call.sessions.set(SESSION, Posts(posts.joinToString("|")))
                    call.respondRedirect(ROUTE_POSTS)

                } else {
                    call.respond(FreeMarkerContent(INDEX_PAGE, mapOf("error" to "Invalid Post")))
                }
            }
        }
    }
}

const val INDEX_PAGE = "index.ftl"
const val ROUTE_BLOG = "/blog"
const val ROUTE_POSTS = "/posts"
const val ROUTE_STATIC = "/static"
const val SESSION = "session_blog"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val DATE = "date"
const val MESSAGE_BAD_REQUEST = "Invalid session"

fun String?.validate() = this?.isNotEmpty() ?: false

val posts = mutableListOf<PostBlog>()

data class Posts(
    val posts: String
)

data class PostBlog(
    val title: String,
    val description: String,
    val date: String
)
