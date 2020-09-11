package co.devhack.usecases

import co.devhack.common.Either
import co.devhack.common.Failure

abstract class UseCaseBase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    suspend fun execute(params: Params, onResult: suspend (Either<Failure, Type>) -> Unit = {}) =
        onResult(run(params))

    class None
}
