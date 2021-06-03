package com.microservices.centralservice.persistence

import com.microservices.centralservice.model.Central
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CentralRepository : ReactiveCrudRepository<Central, UUID>
