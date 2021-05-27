package com.microservices.projectservice.service

import com.microservices.projectservice.exception.BadRequestException
import com.microservices.projectservice.exception.NotFoundException
import com.microservices.projectservice.model.Project
import com.microservices.projectservice.persistence.ProjectRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ProjectServiceTest(
    @Mock private val repository: ProjectRepository
) {
    private val service = ProjectService(repository)

    private fun getTestIssue(id: UUID?): Project {
        return Project(
            id,
            "testName",
            null
        )
    }

    private fun mockRepositorySave(id: UUID) {
        Mockito.`when`(repository.save(any<Project>())).then {
            val hopefullyIssue = it.arguments.first()
            if (hopefullyIssue is Project) {
                hopefullyIssue.id = id
            }
            Mono.just(hopefullyIssue)
        }
    }

    @Test
    fun testShouldCreate() {
        val id = UUID.randomUUID()
        val testProject = getTestIssue(null)

        mockRepositorySave(id)

        StepVerifier
            .create(service.create(testProject))
            .consumeNextWith { i ->
                assert(i.name == testProject.name)
                assert(i.description == testProject.description)
                assert(i.id != null)
                Mockito.verify(repository).save(testProject)
            }
            .verifyComplete()
    }

    @Test
    fun testShouldGet() {
        val id = UUID.randomUUID()
        val testProject = getTestIssue(id)

        given(repository.findById(id)).willReturn(Mono.just(testProject))

        StepVerifier
            .create(service.get(id))
            .consumeNextWith { i ->
                assert(i.id == id)
                assert(i.name == testProject.name)
                assert(i.description == testProject.description)
                Mockito.verify(repository).findById(id)
            }
            .verifyComplete()
    }

    @Test
    fun testShouldThrowNotFoundExceptionOnGet() {
        val id = UUID.randomUUID()

        given(repository.findById(id)).willReturn(Mono.empty())

        StepVerifier
            .create(service.get(id))
            .expectErrorMatches { e ->
                e is NotFoundException
            }
            .verify()
    }

    @Test
    fun testShouldUpdate() {
        val id = UUID.randomUUID()
        val testProject = getTestIssue(id)
        val updateIssue = Project(
            id,
            "updatedName",
            "updatedDescription"
        )

        given(repository.findById(id)).willReturn(Mono.just(testProject))
        mockRepositorySave(id)

        StepVerifier
            .create(service.update(id, updateIssue))
            .consumeNextWith { i ->
                assert(i.id == testProject.id)
                assert(i.name == updateIssue.name)
                assert(i.description == updateIssue.description)
                Mockito.verify(repository).save(testProject)
            }
            .verifyComplete()
    }

    @Test
    fun testShouldThrowNotFoundExceptionOnUpdate() {
        val id = UUID.randomUUID()
        val testProject = getTestIssue(id)

        given(repository.findById(id)).willReturn(Mono.empty())

        StepVerifier
            .create(service.update(id, testProject))
            .expectErrorMatches { e ->
                e is NotFoundException
            }
            .verify()
    }

    @Test
    fun testShouldThrowBadRequestExceptionOnUpdate() {
        val id = UUID.randomUUID()
        val secondId = UUID.randomUUID()
        val testProject = getTestIssue(secondId)

        StepVerifier
            .create(service.update(id, testProject))
            .expectErrorMatches { e ->
                e is BadRequestException
            }
            .verify()
    }

    @Test
    fun testShouldDelete() {
        val id = UUID.randomUUID()
        val testProject = getTestIssue(id)

        given(repository.findById(id)).willReturn(Mono.just(testProject))
        given(repository.delete(testProject)).willReturn(Mono.empty())

        StepVerifier
            .create(service.delete(id))
            .consumeNextWith { i ->
                Mockito.verify(repository).findById(id)
                Mockito.verify(repository).delete(testProject)
            }
            .verifyComplete()
    }

    @Test
    fun testShouldThrowNotFoundExceptionOnDelete() {
        val id = UUID.randomUUID()

        given(repository.findById(id)).willReturn(Mono.empty())

        StepVerifier
            .create(service.delete(id))
            .expectErrorMatches { e ->
                e is NotFoundException
            }
            .verify()
    }
}
