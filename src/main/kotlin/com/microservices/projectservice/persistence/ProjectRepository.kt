package com.microservices.projectservice.persistence

import com.microservices.projectservice.model.Project
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProjectRepository : ReactiveCrudRepository<Project, UUID>
