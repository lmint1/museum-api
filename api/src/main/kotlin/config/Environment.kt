package config

data class Environment(
    val apikey: String = System.getenv("apikey"),
    val baseUrl: String = System.getenv("baseUrl"),
    val isDebug: Boolean = System.getenv("environment") == "DEBUG",
    val dbHost: String = System.getenv("dbHost"),
    val dbUserName: String = System.getenv("dbUserName"),
    val dbPassword: String = System.getenv("dbPassword"),
    val secret: String = System.getenv("secret"),
)