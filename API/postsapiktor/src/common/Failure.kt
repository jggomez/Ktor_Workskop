package co.devhack.common

sealed class Failure {

    object NetworkConnection : Failure()
    class ServerError(val ex: Exception) : Failure()
    class BadRequest(val message: String) : Failure()
    object CustomError : Failure()

    abstract class FeatureFailure : Failure()

}