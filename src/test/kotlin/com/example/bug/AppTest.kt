package com.example.bug

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.kotlin.test.test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = [AppTest.PropsInit::class])
internal class AppTest {

    @Autowired
    private lateinit var repository: Repository

    @Test
    fun test() {
        repository
            .save(EntityOne("1"))
            .test()
            .assertNext {
                Assertions.assertEquals(EntityType.FIRST, it.type)
            }
            .verifyComplete()

        repository
            .findAll()
            .next()
            .test()
            .assertNext {
                Assertions.assertEquals("1", it.id)
                Assertions.assertEquals(EntityType.FIRST, it.type)
            }
            .verifyComplete()
    }

    companion object {
        @Container
        val mongoDbContainer = GenericContainer<Nothing>("percona/percona-server-mongodb:4.4.4-6")
            .apply {
                addExposedPort(27017)
            }
    }

    class PropsInit : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.data.mongodb.host=${mongoDbContainer.containerIpAddress}",
                "spring.data.mongodb.port=${mongoDbContainer.firstMappedPort}"
            )
        }
    }
}