package config

import java.lang.Exception
import java.net.URI

data class Environment(
    val apikey: String = System.getenv("API_KEY"),
    val baseUrl: String = System.getenv("BASE_URL"),
    val isDebug: Boolean = System.getenv("IS_DEBUG") == "DEBUG",
    var dbHost: String = System.getenv("DATABASE_URL"),
    var dbUserName: String = getenvOrNull("DATABASE_USER") ?: "",
    var dbPassword: String = getenvOrNull("DATABASE_PASSWORD") ?: "",
    val secret: String = System.getenv("SECRET"),
) {

    init {
        val dbUri = URI(dbHost)
        dbUserName = dbUri.userInfo.split(":")[0]
        dbPassword = dbUri.userInfo.split(":")[1]
        dbHost = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.path.toString() + "?sslmode=require"
    }

}

private fun getenvOrNull(name: String): String? {
    return try { System.getenv(name) } catch (e: Exception) { null }
}