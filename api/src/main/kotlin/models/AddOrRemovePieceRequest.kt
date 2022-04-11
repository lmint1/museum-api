package models

import kotlinx.serialization.Serializable

@Serializable
data class AddOrRemovePieceRequest(val objectId: Long)
