package com.lukaslechner.coroutineusecasesonandroid.playground.flow.channels

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 *     Channels -
 *
 *     Low-level inter-coroutine communication primitive.
 *
 *     Flows and some flow operators are built on top of Channels.
 *
 *     Events are consumed exactly once by a Single Subscriber.
 *
 *     No "typical" use case in Android Apps.
 */

suspend fun main(): Unit = coroutineScope {

        val channel: ReceiveChannel<Int> = produce<Int> {
            println("Sending 10")
            send(10)

            println("Sending 20")
            send(20)

            println("Sending 30")
            send(30)

            println("Sending 40")
            send(40)
        }

        launch {
            channel.consumeEach { receivedValue ->
                println("Consumer1: $receivedValue")
            }
        }

    launch {
        channel.consumeEach { receivedValue ->
            println("Consumer2: $receivedValue")
        }
    }
}