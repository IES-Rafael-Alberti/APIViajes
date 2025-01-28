package com.es.apiSpringBoot.exception

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)