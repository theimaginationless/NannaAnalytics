package com.theimless.nannaanalytics.metric.dto

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class BaseEvent(
    val section: String = "",
    val createdDate: Date? = null,
    val content: String = ""
)