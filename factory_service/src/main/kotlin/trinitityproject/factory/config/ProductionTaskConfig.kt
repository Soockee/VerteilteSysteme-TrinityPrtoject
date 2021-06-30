package trinitityproject.factory.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ProductionTaskConfig(
    @Value("\${supplier.host}") val supplierHost: String,
    @Value("\${supplier.port}") val supplierPort: String,
) {
    @Bean
    fun jacksonWebClient(): WebClient {
        //The jackson serializer and deserializer is set for the webclient which sends the http requests
        val jacksonStrategies = ExchangeStrategies
            .builder()
            .codecs { clientDefaultCodecsConfigurer: ClientCodecConfigurer ->
                clientDefaultCodecsConfigurer.defaultCodecs()
                    .jackson2JsonEncoder(Jackson2JsonEncoder(ObjectMapper(), MediaType.APPLICATION_JSON))
                clientDefaultCodecsConfigurer.defaultCodecs()
                    .jackson2JsonDecoder(Jackson2JsonDecoder(ObjectMapper(), MediaType.APPLICATION_JSON))
            }.build()

        //The WebClient for the production task
        @Suppress("HttpUrlsUsage")
        return WebClient
            .builder()
            .baseUrl("http://$supplierHost:$supplierPort")
            .exchangeStrategies(jacksonStrategies)
            .build()
    }
}