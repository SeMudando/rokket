package br.com.semudando.rokket.util

import kotlinx.coroutines.delay

internal class ReconnectWaitService {
  private var waitingTime = -1

  companion object {
    val instance = ReconnectWaitService()
  }

  fun resetWaitingTime() {
    this.waitingTime = -1
  }

  suspend fun wait() {
    val waitingTime = this.getWaitingTime()

    delay(waitingTime * 1000L)
  }

  private fun getWaitingTime(): Int {
    val waitingTimes = listOf(5, 10, 30)

    val newWaitingTime = if (waitingTime == waitingTimes.last()) {
      waitingTimes.last()
    } else {
      waitingTimes[waitingTimes.indexOf(waitingTime) + 1]
    }

    this.waitingTime = newWaitingTime
    return newWaitingTime
  }
}
