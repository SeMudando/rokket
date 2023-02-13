package at.rueckgr.kotlin.rocketbot.handler.message

import at.rueckgr.kotlin.rocketbot.BotConfiguration
import at.rueckgr.kotlin.rocketbot.EventHandler
import at.rueckgr.kotlin.rocketbot.util.ReconnectWaitService
import at.rueckgr.kotlin.rocketbot.websocket.LoginMessage
import at.rueckgr.kotlin.rocketbot.websocket.PasswordData
import at.rueckgr.kotlin.rocketbot.websocket.UserData
import at.rueckgr.kotlin.rocketbot.websocket.WebserviceRequestParam
import com.fasterxml.jackson.databind.JsonNode

@Suppress("unused")
class ConnectedMessageHandler(eventHandler: EventHandler, botConfiguration: BotConfiguration)
        : AbstractMessageHandler(eventHandler, botConfiguration) {
    override fun getHandledMessage() = "connected"

    override fun handleMessage(data: JsonNode): Array<Any> {
        val digest = botConfiguration.password

        ReconnectWaitService.instance.resetWaitingTime()
        PingMessageHandler.updateLastPing()

        return arrayOf(
            LoginMessage(
                id = "login-initial",
                params = arrayOf(
                    WebserviceRequestParam(
                        UserData(botConfiguration.username),
                        PasswordData(digest, "sha-256")
                    )
                )
            )
        )
    }
}
