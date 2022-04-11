package config

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.LogRequestInterceptor
import com.github.kittinunf.fuel.core.interceptors.LogResponseInterceptor
import controllers.PieceController
import controllers.UserController
import gateway.PieceGateway
import infrastructure.PieceGatewayImpl
import infrastructure.PieceRepositoryImpl
import infrastructure.UserRepositoryImpl
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.koin.core.scope.get
import org.koin.dsl.module
import repository.PieceRepository
import repository.UserRepository
import security.AuthManager
import services.PieceService
import services.UserService
import usecase.*

val apiModule = module {
    single { Environment() }
    single(createdAtStart = true) { getDatabase(get()) }
    single(createdAtStart = true) { createClient(get()) }
    single(createdAtStart = true) { AuthManager(get()) }
    single<PieceGateway> { PieceGatewayImpl(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl() }
    single<PieceRepository> { PieceRepositoryImpl() }
    factory { createJson() }
    single { UserController(get(), get()) }
    single { UserService(get(), get(), get(), get(), get()) }
    single { PieceController(get()) }
    single { PieceService(get(), get()) }
}

val useCaseModule = module {
    factory { AddPieceToFavorite(get(), get()) }
    factory { ListAllPieces(get()) }
    factory { ListUserPieces(get()) }
    factory { RemovePieceFromFavorite(get(), get()) }
    factory { VotePiece(get()) }
}

/** Fuel configuration **/
private fun createJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

private fun createClient(env: Environment): Fuel {
    val manager = FuelManager.instance.reset()
    with(manager) {
        basePath = env.baseUrl
        if (env.isDebug) addLoggers()
    }
    return Fuel
}

private fun FuelManager.addLoggers() {
    addRequestInterceptor(LogRequestInterceptor)
    addResponseInterceptor(LogResponseInterceptor)
}

/** Exposed configuration **/
// Ensures to create only one connection and avoiding memory leak
private var database: Database? = null

fun getDatabase(env: Environment): Database {
    if (database == null) createDatabase(env)
    return database!!
}

private fun createDatabase(env: Environment) = with(env) {
    val driver = "com.impossibl.postgres.jdbc.PGDriver"
    database = Database.connect(dbHost, driver, dbUserName, dbPassword)
}
