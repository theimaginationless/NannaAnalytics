package com.theimless.nannaanalytics.metric.service

import com.theimless.nannaanalytics.metric.dto.BaseEvent
import com.theimless.nannaanalytics.metric.model.BaseEventModel
import com.theimless.nannaanalytics.metric.repository.MetricRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Slf4j
@Service
open class SimpleMetricHandlerService @Autowired constructor(
        private val metricRepository: MetricRepository
    ) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Async
    open fun sendEventToQueue(event: BaseEvent) {
        log.info("Event saving to section ${event.section}")
        val eventModel = BaseEventModel()
        eventModel.content = event.content
        eventModel.createdDate = event.createdDate
        metricRepository.save(eventModel)
    }

    open fun getAll(): List<BaseEventModel> {
        return metricRepository.findAll()
    }
}

