package com.theimless.nannaanalytics.metric.dto

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    val statusCode: Int
)