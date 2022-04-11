package security

import io.javalin.plugin.openapi.dsl.ApplyUpdates
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation
import io.javalin.plugin.openapi.dsl.createUpdater
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.security.SecurityRequirement

fun OpenApiDocumentation.secureOperation(applyUpdates: ApplyUpdates<Operation>) = apply {
    operation(createUpdater(applyUpdates)).operation {
        it.security = listOf(SecurityRequirement().addList("bearerAuth"))
    }
}