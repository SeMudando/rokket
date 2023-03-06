package br.com.semudando.rokket

import io.kotest.core.listeners.AfterContainerListener
import io.kotest.core.listeners.BeforeContainerListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.util.logging.Logger

//TODO Publish this library independently
public class RocketChatListener(
  public val adminUsername: String = "test_admin",
  public val adminName: String = "test_admin",
  public val adminPassword: String = "pass",
  public val adminEmail: String = "admin@test.com",
  public val mongoVersion: String = "5.0",
  public val rocketVersion: String = "5.4.0",
) : BeforeContainerListener, AfterContainerListener, TestListener {

  private val network = Network.newNetwork()

  private val mongoDb = MongoDBContainer("mongo:$mongoVersion")
    .withNetwork(network)
    .withNetworkAliases("mongo")


  private val rocket = GenericContainer("rocket.chat:$rocketVersion")
    .dependsOn(mongoDb)
    .withNetwork(network)
    .withExposedPorts(3000)
    .waitingFor(Wait.forListeningPort())
    .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("RocketChatListener")))
    .withEnv(
      mapOf(
        "ROOT_URL" to "http://localhost:3000",
        "MONGO_URL" to "mongodb://mongo:27017/rocketchat?directConnection=true",
        "MONGO_OPLOG_URL" to "mongodb://mongo:27017/local?directConnection=true",
        "ADMIN_USERNAME" to adminUsername,
        "ADMIN_NAME" to adminName,
        "ADMIN_PASS" to adminPassword,
        "ADMIN_EMAIL" to adminEmail,
        "OVERWRITE_SETTING_Show_Setup_Wizard" to "completed",
      )
    )

  val host: String
    get() = rocket.getHost()

  val port: Int
    get() = rocket.getFirstMappedPort()

  override suspend fun beforeSpec(spec: Spec) {
    mongoDb.start()
    rocket.start()  }

  override suspend fun afterSpec(spec: Spec) {
    rocket.stop()
    mongoDb.stop()
  }

}

suspend fun main() {
  RocketChatListener()
  delay(100_000)
}
