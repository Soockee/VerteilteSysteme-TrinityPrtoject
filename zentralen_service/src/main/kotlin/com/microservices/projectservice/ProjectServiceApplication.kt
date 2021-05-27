package com.microservices.projectservice

import com.microservices.projectservice.config.ProjectServiceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories
@EnableConfigurationProperties(ProjectServiceProperties::class)
class ProjectServiceApplication

fun main(args: Array<String>) {
    runApplication<ProjectServiceApplication>(*args)
}
