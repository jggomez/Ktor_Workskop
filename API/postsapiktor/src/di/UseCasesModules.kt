package co.devhack.di

import co.devhack.usecases.CreatePostUseCase
import co.devhack.usecases.GetAllPostUseCase
import org.koin.dsl.module

val postsUseCaseModule = module(createdAtStart = true) {
    factory {
        CreatePostUseCase(get())
    }

    factory {
        GetAllPostUseCase(get())
    }
}