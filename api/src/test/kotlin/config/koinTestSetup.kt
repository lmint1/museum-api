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
private val environment = Environment(
    apikey = TODO("define your apikey"),
    baseUrl = TODO("define your baseUrl"),
    isDebug = TODO("define your isDebug"),
    dbHost = TODO("define your dbHost"),
    dbUserName = TODO("define your dbUserName"),
    dbPassword = TODO("define your dbPassword"),
    secret = TODO("define your secret"),
)