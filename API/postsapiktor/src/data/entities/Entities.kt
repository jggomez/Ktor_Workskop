package co.devhack.data.entities

import co.devhack.usecases.domain.Post

data class PostEntity(
    val title: String = "",
    val description: String = "",
    val date: String = ""
) {
    fun toPost() =
        Post(
            title,
            description,
            date
        )
}
