import config.apiModule
import config.useCaseModule
import controllers.addPieceController
import controllers.addUserController
import infrastructure.database.Passwords
import infrastructure.database.Pieces
import infrastructure.database.Users
import io.javalin.Javalin
import io.javalin.core.JavalinConfig
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.Koin
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import security.AuthManager
import security.Role

fun main() {
    val modules = listOf(apiModule, useCaseModule)
    val koin = startKoin { loadKoinModules(modules) }.koin
    transaction { SchemaUtils.create (Users, Passwords, Pieces) }
    startApp(7070, koin)
}

fun startApp(port: Int, koin: Koin) = with(koin) {
    // Configuring controllers passing the services
    Javalin.create { setup(it, get()) }.start(port)
        .addUserController(get())
        .addPieceController(get())
}

private fun setup(config: JavalinConfig, manager: AuthManager) {
    config.accessManager(manager::accessHandler)
    val appInfo = Info().version("1.0").description("Museum API")
    val options = OpenApiOptions { OpenAPI().components(securityComponents()).info(appInfo) }
        .path("/swagger-docs")
        .roles(Role.ANYONE)
        .swagger(SwaggerOptions("/swagger").title("Museum API Documentation"))
        .disableCaching()
    config.registerPlugin(OpenApiPlugin(options))
}

private fun securityComponents(): Components {
    val securityScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
    return Components().addSecuritySchemes("bearerAuth", securityScheme)
}
