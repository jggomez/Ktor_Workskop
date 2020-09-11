package co.devhack.endpoints.dto

data class DTOError(
    val code: String,
    val message: String
)

data class DTOSuccessful(
    val successful: Boolean
)