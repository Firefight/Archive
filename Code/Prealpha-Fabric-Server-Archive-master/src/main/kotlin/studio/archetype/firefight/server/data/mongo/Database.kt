package studio.archetype.firefight.server.data.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import studio.archetype.firefight.server.data.config.CardinalServerConfig

object Database {
    lateinit var mongoClient: MongoClient

    fun connect() {
        mongoClient = KMongo.createClient(CardinalServerConfig.instance.mongoUri)
    }

    fun database(): MongoDatabase = mongoClient.getDatabase(CardinalServerConfig.instance.dbName)
    inline fun <reified T> coll(name: String): MongoCollection<T> = database().getCollection(name, T::class.java)
}