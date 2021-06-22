package trinitityproject.factory.repository

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories


@Configuration
@EnableReactiveMongoRepositories(
    basePackageClasses = [ProductOrderRepository::class]
)
class MongoConfig : AbstractReactiveMongoConfiguration() {


    @Value("\${spring.data.mongodb.uri}")
    lateinit var uri: String

    @Value("\${spring.data.mongodb.database}")
    lateinit var dbName: String

    override fun getDatabaseName() = dbName

    override fun reactiveMongoClient() = mongoClient()

    override fun configureClientSettings(builder: MongoClientSettings.Builder) {
        super.configureClientSettings(builder)
        builder.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
    }

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create(
        MongoClientSettings
            .builder()
            .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
            .applyConnectionString(ConnectionString(uri + dbName))
            .build()
    )

    @Bean
    override fun reactiveMongoTemplate(
        databaseFactory: ReactiveMongoDatabaseFactory,
        mongoConverter: MappingMongoConverter
    ): ReactiveMongoTemplate = ReactiveMongoTemplate(mongoClient(), databaseName)


}