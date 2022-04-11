package security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import config.Environment
import io.javalin.core.security.RouteRole
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import java.lang.Exception

class AuthManager(env: Environment) {

    private val algorithm = Algorithm.HMAC256(env.secret)

    fun accessHandler(handler: Handler, ctx: Context, permittedRoles: Set<RouteRole>) {
        when {
            Role.ANYONE in permittedRoles -> handler.handle(ctx)
            isTokenValid(ctx, permittedRoles) -> handler.handle(ctx)
            else -> ctx.status(401).json(UnauthorizedResponse())
        }
    }

    private fun isTokenValid(ctx: Context, permittedRoles: Set<RouteRole>): Boolean {
        return try {
            val decoded = decodeToken(ctx)
            val role = decoded.getRoleOrNull()!!
            role in permittedRoles
        } catch (e: Exception) {
            false
        }
    }

    fun decodeToken(ctx: Context): DecodedJWT? {
        return try {
            val verifier = JWT.require(algorithm).build()
            verifier.verify(ctx.token!!)
        } catch (e: Exception) {
            null
        }
    }

    private val Context.token: String? get() = header("Authorization")
        ?.replace("Bearer ", "")

    private fun DecodedJWT?.getRoleOrNull(): Role? {
        val role = this?.getClaim("role")?.asString()
        return role?.let(Role::valueOf)
    }

    fun createToken(userId: String, role: Role): String? {
        val token = JWT.create()
            .withClaim("uuid", userId)
            .withClaim("role", role.name)
        return token.sign(algorithm)
    }

}