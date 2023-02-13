package br.com.semudando.rokket

import br.com.semudando.rokket.exception.LoginException
import br.com.semudando.rokket.exception.TerminateWebsocketClientException
import br.com.semudando.rokket.handler.message.AbstractMessageHandler
import br.com.semudando.rokket.util.MessageHelper
import br.com.semudando.rokket.util.ReconnectWaitService
import br.com.semudando.rokket.websocket.ConnectMessage
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
  private val botConfiguration: br.com.semudando.rokket.BotConfiguration,
  private val eventHandler: br.com.semudando.rokket.EventHandler,
  private val healthChecker: br.com.semudando.rokket.HealthChecker,
) {
    companion object {
        val webserviceMessageQueue = ArrayBlockingQueue<br.com.semudando.rokket.WebserviceMessage>(10)
        val subscriptionService = br.com.semudando.rokket.SubscriptionService()
        val statusService = br.com.semudando.rokket.StatusService()
        var userId: String? = null
        var authToken: String? = null
        var host: String = ""
    }

    fun start() {
        br.com.semudando.rokket.Bot.Companion.statusService.healthChecker = this.healthChecker
        br.com.semudando.rokket.Bot.Companion.statusService.startDate = LocalDateTime.now()

        br.com.semudando.rokket.Bot.Companion.host = botConfiguration.host

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
            br.com.semudando.rokket.Bot.Companion.subscriptionService.reset()
        }
    }

    private suspend fun DefaultClientWebSocketSession.waitForWebserviceInput() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                val webserviceInput = br.com.semudando.rokket.Bot.Companion.webserviceMessageQueue.poll(5, TimeUnit.SECONDS) ?: continue
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
                    .getDeclaredConstructor(br.com.semudando.rokket.EventHandler::class.java, br.com.semudando.rokket.BotConfiguration::class.java)
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

