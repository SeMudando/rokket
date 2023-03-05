package br.com.semudando.rokket

import kotlinx.coroutines.delay
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.Network

class RocketChatExtension {

  val network = Network.newNetwork()

  val mongoDb = MongoDBContainer("mongo:4.4.15")
    .withNetwork(network)
    .withNetworkAliases("mongo")
    .withExposedPorts(27017)
    .also { it.start() }


  val rocket: GenericContainer<*> = GenericContainer("rocket.chat:5.4")
    .dependsOn(mongoDb)
    .withNetwork(network)
    .withExposedPorts(3000)
    .withEnv(
      mapOf(
        "ROOT_URL" to "http://localhost:3000",
        "MONGO_URL" to "mongodb://mongo:27017/rocketchat?directConnection=true",
        "MONGO_OPLOG_URL" to "mongodb://mongo:27017/local?directConnection=true",
        "ADMIN_USERNAME" to "test",
        "ADMIN_NAME" to "test",
        "ADMIN_PASS" to "test",
        "ADMIN_EMAIL" to "test@test.com",
        "OVERWRITE_SETTING_Show_Setup_Wizard" to "completed",
      )
    )

}

suspend fun main() {
  RocketChatExtension().apply {
    mongoDb.execInContainer("mongo", "--eval", "\"printjson(rs.initiate())\"")
    rocket.withStartupAttempts(5).start()
  }
  delay(100_000)
}
