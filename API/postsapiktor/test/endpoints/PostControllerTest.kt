package endpoints

import co.devhack.endpoints.dto.DTOSuccessful
import co.devhack.posts
import co.devhack.usecases.domain.Post
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.server.testing.*
import org.amshove.kluent.`should be greater than`
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object PostControllerTest : Spek({

    Feature("Get All Users") {
        Scenario("Should get all users correctly") {

            lateinit var resp: TestApplicationResponse

            Given("Create stubs") {
                // Should create users in the DB
            }

            When("Get all users") {
                withTestApplication({
                    posts()
                }) {
                    handleRequest(HttpMethod.Get, PostControllerTest.URI_API) {
                        addHeader("Content-Type", "application/json")
                    }.apply {
                        resp = response
                    }
                }
            }

            Then("Verify status") {
                resp shouldNotBe null
                resp.status() shouldBeEqualTo HttpStatusCode.OK
            }

            Then("Verify answers") {
                resp shouldNotBe null
                val successfulDTO =
                    jacksonObjectMapper().readValue<List<Post>>(resp.content,
                        object : TypeReference<List<Post>?>() {})
                successfulDTO shouldNotBe null
                successfulDTO.size `should be greater than` 0
            }

            afterScenario {
                // delete users or data created
            }
        }
    }

    Feature("Register an user") {
        Scenario("Should register an user correctly") {

            lateinit var resp: TestApplicationResponse
            lateinit var post: Post

            Given("Create stubs") {
                // Should create users in the DB
                post = Post(
                    title = PostControllerTest.TITLE_FAKE,
                    description = PostControllerTest.DESCRIPTION_FAKE,
                    date = PostControllerTest.DATE_FAKE
                )
            }

            When("Register an user") {
                withTestApplication({
                    posts()
                }) {
                    handleRequest(HttpMethod.Post, PostControllerTest.URI_API) {
                        addHeader("Content-Type", "application/json")
                        setBody(
                            jacksonObjectMapper().writeValueAsString(post)
                        )
                    }.apply {
                        resp = response
                    }
                }
            }

            Then("Verify status") {
                resp shouldNotBe null
                resp.status() shouldBeEqualTo HttpStatusCode.OK
            }

            Then("Verify answers") {
                resp shouldNotBe null
                val successfulDTO =
                    jacksonObjectMapper().readValue(resp.content, DTOSuccessful::class.java)
                successfulDTO shouldNotBe null
                successfulDTO.successful shouldBeEqualTo true
            }

            afterScenario {
                // delete users or data created
            }
        }
    }
}) {

    private const val URI_API = "/posts"
    private const val TITLE_FAKE = "Title test"
    private const val DESCRIPTION_FAKE = "Description test"
    private const val DATE_FAKE = "01/01/2012"
}