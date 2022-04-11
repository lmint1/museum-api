package models

data class TokenResponse(val token: String, val type: String = "Bearer")
