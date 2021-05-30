package dev.cbyrne.indi.database

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import dev.cbyrne.indi.database.model.Guild
import dev.cbyrne.indi.database.model.User
import org.litote.kmongo.KMongo
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.updateOne
import kotlin.collections.set
import net.dv8tion.jda.api.entities.Guild as DiscordGuild

object Database {
    private val client = KMongo.createClient()

    private lateinit var database: MongoDatabase
    private lateinit var guildsCollection: MongoCollection<Guild>
    private lateinit var usersCollection: MongoCollection<User>

    private var guildsCache = mutableMapOf<Long, Guild?>()
    private var usersCache = mutableMapOf<Long, User?>()

    fun initialise() {
        database = client.getDatabase("indi")
        guildsCollection = database.getCollection<Guild>("guilds")
        usersCollection = database.getCollection<User>("users")
    }

    fun getGuild(id: Long): Guild? {
        val cached = guildsCache[id]
        if (cached != null) return cached

        val guild = guildsCollection.findOneById(id)
        guildsCache[id] = guild

        return guild
    }

    fun insertGuild(guild: DiscordGuild): Guild {
        val document = Guild(guild.idLong, guild.name)

        guildsCollection.insertOne(document)
        guildsCache[document._id] = document

        return document
    }

    fun updateGuild(guild: Guild) {
        guildsCollection.updateOne(guild)
        guildsCache.remove(guild._id)
    }

    fun getOrInsertGuild(guild: DiscordGuild) = getGuild(guild.idLong) ?: insertGuild(guild)

    fun getUser(id: Long): User? {
        val cached = usersCache[id]
        if (cached != null) return cached

        val superuser = usersCollection.findOneById(id)
        usersCache[id] = superuser

        return superuser
    }

    fun insertUser(id: Long): User {
        val document = User(id, false, false)

        usersCollection.insertOne(document)
        usersCache[document._id] = document

        return document
    }

    fun updateUser(user: User) {
        usersCollection.updateOne(user)
        usersCache.remove(user._id)
    }

    fun getAllUsers(): List<User> {
        val users = usersCollection.find().toList()
        usersCache = users.associateBy { it._id }.toMutableMap()

        return users
    }

    fun getOrInsertUser(id: Long) = getUser(id) ?: insertUser(id)

    fun clearCache() {
        usersCache.clear()
        guildsCache.clear()
    }
}