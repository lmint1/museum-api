package infrastructure

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.serialization.responseObject
import com.github.kittinunf.result.Result
import config.Environment
import dto.PieceItem
import gateway.PieceGateway
import kotlinx.serialization.json.Json
import models.PieceItemResponse
import models.PiecesResponse
import java.lang.Exception

class PieceGatewayImpl(
    private val client: Fuel,
    private val json: Json,
    environment: Environment
): PieceGateway {

    private val apiKey: String = environment.apikey

    override fun get(id: Long): PieceItem {
        val request = client.get("object/$id").withParams(paramsOf())
        val (_, _, result) = request.responseObject<PieceItemResponse>(json)
        return when (result) {
            is Result.Success -> result.value
            is Result.Failure -> throw Exception(result.error.message)
        }
    }

    override fun getAll(pageSize: Int, page: Int): List<PieceItem> {
        val params = paramsOf("page" to page, "size" to pageSize, "sort" to "random")
        val request = client.get("object").withParams(params)
        val (_, _, result) = request.responseObject<PiecesResponse>(json)
        return when (result) {
            is Result.Success -> result.value.records
            is Result.Failure -> throw Exception(result.error.message)
        }
    }

    private fun Request.withParams(params: List<Pair<String, Any>>) =
        apply { parameters = params }

    private fun paramsOf(vararg values: Pair<String, Any>) =
        values.toList() + ("apikey" to apiKey)

}
