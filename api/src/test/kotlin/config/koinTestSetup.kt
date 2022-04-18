package config

import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val koinSetup: KoinAppDeclaration = {
    allowOverride(true)
    modules(apiModule, testModule, useCaseModule)
}

private val testModule = module {
    single(createdAtStart = true){ environment }
}

// You can set your environment variables here
private val environment = Environment()