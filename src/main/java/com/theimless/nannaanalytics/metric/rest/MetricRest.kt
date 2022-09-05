package com.theimless.nannaanalytics.metric.rest

import com.theimless.nannaanalytics.metric.dto.BaseEvent
import com.theimless.nannaanalytics.metric.dto.BaseResponse
import com.theimless.nannaanalytics.metric.model.BaseEventModel
import com.theimless.nannaanalytics.metric.service.SimpleMetricHandlerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/metric")
open class MetricRest @Autowired constructor(
    private val handlerService: SimpleMetricHandlerService
){
//    private val eventQueue: Queue<BaseEvent> = LinkedList()


    @PostMapping("/send")
    @PreAuthorize("hasAnyAuthority(T(com.theimless.nannaanalytics.common.user.model.RoleAuthority).ROLE_PRODUCER)")
    open fun sendMetric(@RequestBody event: BaseEvent): ResponseEntity<BaseResponse> {
        handlerService.sendEventToQueue(event)
        val rs = BaseResponse(0)
        return ResponseEntity.ok(rs)
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority(" +
            "T(com.theimless.nannaanalytics.common.user.model.RoleAuthority).ROLE_CUSTOMER," +
            "T(com.theimless.nannaanalytics.common.user.model.RoleAuthority).ROLE_ANALYTIC)"
    )
    open fun getQueue(): ResponseEntity<List<BaseEvent>> {
        val simpleMapped: List<BaseEvent> = handlerService.getAll().map {
            BaseEvent("Lost", it.createdDate, it.content ?: "")
        }
        return ResponseEntity.ok(simpleMapped)
    }
}