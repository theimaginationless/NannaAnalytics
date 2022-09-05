package com.theimless.nannaanalytics.metric.repository

import com.theimless.nannaanalytics.metric.model.BaseEventModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricRepository: JpaRepository<BaseEventModel, Long> {
}