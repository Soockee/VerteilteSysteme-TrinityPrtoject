package trinitityproject.factory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FactoryApplication

fun main(args: Array<String>) {
	runApplication<FactoryApplication>(*args)
}
