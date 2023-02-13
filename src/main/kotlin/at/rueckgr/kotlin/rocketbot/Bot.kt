package at.rueckgr.kotlin.rocketbot

import at.rueckgr.kotlin.rocketbot.exception.LoginException
import at.rueckgr.kotlin.rocketbot.exception.TerminateWebsocketClientException
import at.rueckgr.kotlin.rocketbot.handler.message.AbstractMessageHandler
import at.rueckgr.kotlin.rocketbot.util.MessageHelper
import at.rueckgr.kotlin.rocketbot.util.ReconnectWaitService
import at.rueckgr.kotlin.rocketbot.websocket.ConnectMessage
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.reflections.Reflections
import java.time.LocalDateTime
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit


class Bot(
    private val botConfiguration: BotConfiguration,
    private val eventHandler: EventHandler,
    private val healthChecker: HealthChecker,
) {
    companion object {
        val webserviceMessageQueue = ArrayBlockingQueue<WebserviceMessage>(10)
        val subscriptionService = SubscriptionService()
        val statusService = StatusService()
        var userId: String? = null
        var authToken: String? = null
        var host: String = ""
    }

    fun start() {
        statusService.healthChecker = this.healthChecker
        statusService.startDate = LocalDateTime.now()

        host = botConfiguration.host

        runBlocking { runWebsocketClient() }

    }

    private suspend fun runWebsocketClient() {
        while (true) {
            try {
                val client = HttpClient(CIO) {
                    install(WebSockets)
                }
                client.wss(
                    method = HttpMethod.Get,
                    host = botConfiguration.host,
                    path = "/websocket"
                ) {
                    try {
                        val messageOutputRoutine = async { receiveMessages() }
                        val userInputRoutine = async { sendMessage(ConnectMessage()) }
                        val webserviceMessageRoutine = async { waitForWebserviceInput() }

                        userInputRoutine.await()
                        messageOutputRoutine.await()
                        webserviceMessageRoutine.cancelAndJoin()
                    } catch (e: Exception) {
                    }
                }
            } catch (e: Exception) {
            }

            ReconnectWaitService.instance.wait()


            // we must clear the list of all channels
            // to ensure that upon reconnect, the bot
            // will properly re-subscribe to all channels
            subscriptionService.reset()
        }
    }

    private suspend fun DefaultClientWebSocketSession.waitForWebserviceInput() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                val webserviceInput = webserviceMessageQueue.poll(5, TimeUnit.SECONDS) ?: continue
                sendMessage(
                    MessageHelper.instance.createSendMessage(
                        webserviceInput.roomId!!,
                        webserviceInput.message,
                        botConfiguration.botId,
                        webserviceInput.emoji,
                        webserviceInput.username
                    )
                )

                Thread.sleep(1000L)
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendMessage(message: Any) {
        // TODO implement token refresh

        val jsonMessage = ObjectMapper().writeValueAsString(message)
        send(Frame.Text(jsonMessage))
    }

    private suspend fun DefaultClientWebSocketSession.receiveMessages() {
        val handlers = Reflections(AbstractMessageHandler::class.java.packageName)
            .getSubTypesOf(AbstractMessageHandler::class.java)
            .map {
                it
                    .getDeclaredConstructor(EventHandler::class.java, BotConfiguration::class.java)
                    .newInstance(eventHandler, botConfiguration)
            }
            .associateBy { it.getHandledMessage() }


        try {
            for (message in incoming) {
                launch {
                    if (message !is Frame.Text) {
                        return@launch
                    }
                    val text = message.readText()

                    val data = ObjectMapper().readTree(text)
                    val messageType = data.get("msg")?.textValue() ?: return@launch
                    if (messageType !in handlers) {
                        return@launch
                    }

                    try {
                        handlers[messageType]
                            ?.handleMessage(data)
                            ?.forEach { sendMessage(it) }
                    } catch (e: LoginException) {
                        throw TerminateWebsocketClientException()
                    } catch (e: Exception) {
                    }
                }
            }

        } catch (e: TerminateWebsocketClientException) {
            throw e
        } catch (e: Exception) {
        }
    }
}

