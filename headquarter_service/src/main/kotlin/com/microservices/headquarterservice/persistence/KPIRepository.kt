package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.factory.kpi.Report
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface KPIRepository : ReactiveCrudRepository<Report, UUID>