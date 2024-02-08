package com.rsps

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.rsps.event.Event
import com.rsps.event.EventSenderInitializationException
import com.rsps.event.PublicMessageEvent
import com.rsps.event.SendEventException
import com.rsps.models.OrderServerResponse
import com.rsps.models.VoteServerLatestResponse
import com.rsps.models.VoteServerResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture

/**
 * Class responsible for sending events to a server using HTTP POST requests.
 *
 * @property client The HTTP client used for making POST requests.
 * @property serverUrl The URL of the server to which events will be sent.
 * @property apiKey The API key used for authentication.
 * @property scope The coroutine scope used for launching asynchronous tasks.
 * @throws EventSenderInitializationException If an error occurs during initialization.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
class EventSender(
    private val client: HttpClient,
    private val serverUrl: String,
    private val apiKey: String,
    private val scope: CoroutineScope,
) {
    private val lock = Any()
    private val pendingEvents = ArrayDeque<Event>()
    private val sendIntervalMillis = 2_000L
    private val batchSize = 20

    init {
        require(serverUrl.isNotEmpty()) { "Server Url must not be empty" }
        require(apiKey.isNotEmpty()) { "API key must not be empty" }
        //startSendingJob()
    }

    /**
     * Starts a background job for periodically sending batches of pending events.
     *
     * This function launches a coroutine that runs in the background and repeatedly calls the
     * `sendBatchOfEvents` function at a specified interval. It continues to run until the
     * coroutine is canceled or stops executing.
     */
    private fun startSendingJob() {
        scope.launch {
            while (isActive) {
                sendBatchOfEvents()
                delay(sendIntervalMillis)
            }
        }
    }

    /**
     * Sends a batch of pending events asynchronously.
     *
     * This function processes a batch of pending events, up to a maximum of 500, by invoking the
     * [sendEvent] function for each event. The events are removed from the pending events queue
     * during processing.
     */
    private suspend fun sendBatchOfEvents() {
        val eventsToSend: Array<Event>

        synchronized(lock) {
            if (pendingEvents.isEmpty()) {
                return
            }

            val eventsToSendCount = minOf(batchSize, pendingEvents.size)
            eventsToSend = pendingEvents.take(eventsToSendCount).toTypedArray()
            pendingEvents.removeAll(eventsToSend.toSet())
        }

        sendEvent(eventsToSend)

    }

    /**
     * Sends an event to the configured server asynchronously.
     *
     * @param event The event to be sent.
     * @throws SendEventException If an error occurs while sending the event.
     */
    private suspend fun sendEvent(event: Array<Event>) {
        scope.launch {
            var failed = false
            try {
                val response = client.post("$serverUrl/event") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    setBody(event)
                }

                if (response.status != HttpStatusCode.OK) {
                    failed = true
                }
            } catch (e: Exception) {
                failed = true
            }
            if (failed) {
                synchronized(lock) {
                    pendingEvents.addAll(event)
                }
            }
        }
    }

    fun attemptDonationClaim(username: String): List<OrderServerResponse> {
        val deferred = CompletableFuture<List<OrderServerResponse>>()
        scope.launch {
            try {
                val response = client.post("$serverUrl/order/claim") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    setBody(username)
                }

                val orderItems: List<OrderServerResponse> = if (response.status == HttpStatusCode.OK) {
                    response.body<List<OrderServerResponse>>()
                } else {
                    emptyList()
                }
                deferred.complete(orderItems)
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }
        return deferred.join()
    }

    fun attemptVoteClaim(username: String): List<VoteServerResponse> {
        val deferred = CompletableFuture<List<VoteServerResponse>>()
        scope.launch {
            try {
                val response = client.post("$serverUrl/vote/claim") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    setBody(username)
                }

                val orderItems: List<VoteServerResponse> = if (response.status == HttpStatusCode.OK) {
                    response.body<List<VoteServerResponse>>()
                } else {
                    emptyList()
                }
                deferred.complete(orderItems)
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }
        return deferred.join()
    }

    fun getLastVoteTimes(username: String): List<VoteServerLatestResponse> {
        val deferred = CompletableFuture<List<VoteServerLatestResponse>>()
        scope.launch {
            try {
                val response = client.post("$serverUrl/vote/last/server") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    setBody(username)
                }

                val orderItems: List<VoteServerLatestResponse> = if (response.status == HttpStatusCode.OK) {
                    response.body<List<VoteServerLatestResponse>>()
                } else {
                    emptyList()
                }
                deferred.complete(orderItems)
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }
        return deferred.join()
    }

    /**
     * Adds an event to the queue for asynchronous processing.
     *
     * This function allows you to enqueue an event for later processing in a thread-safe manner.
     * The event will be added to a list of pending events and can be processed by a separate
     * mechanism or background task.
     *
     * @param event The [Event] to be added to the queue for processing.
     *
     * Example usage for adding a series of events to the queue:
     *
     * ```kotlin
     * val numberOfRequests = 1_000
     * val events = mutableListOf<Event>()
     * for (i in 1..numberOfRequests) {
     *     val event = PublicMessageEvent("Loading $i of $numberOfRequests events", "Leviticus")
     *     events.add(event)
     * }
     * for (event in events) {
     *     getEventSender().queueEvent(event)
     * }
     * ```
     */
    fun queueEvent(event: Event) {
        if (true) {
            return
        }
        synchronized(lock) {
            pendingEvents.add(event)
        }
    }

    fun loadTest() {
        val numberOfRequests = 100
        val events = mutableListOf<Event>()
        for (i in 1..numberOfRequests) {
            val event = PublicMessageEvent("Loading $i of $numberOfRequests events", "Leviticus")
            events.add(event)
        }
        for (event in events) {
            queueEvent(event)
        }
    }

    /**
     * Shuts down the EventSender, waiting for child coroutines to finish,
     * closing the HttpClient, and canceling the scope.
     */
    fun shutdown() {
        runBlocking {
            val childJobs = mutableListOf<Job>()

            if (scope is Job) {
                for (child in scope.children) {
                    childJobs.add(child)
                }
            }

            childJobs.forEach { it.join() }

            client.close()

            scope.cancel()
        }
    }

    companion object {
        /**
         * Initializes an [EventSender] with the specified server URL and API key.
         *
         * @param serverUrl The URL of the server to which events will be sent.
         * @param apiKey The API key used for authentication.
         * @return An instance of [EventSender].
         * @throws EventSenderInitializationException If an error occurs during initialization.
         */
        @JvmStatic
        fun init(serverUrl: String, apiKey: String): EventSender {
            try {
                val client = HttpClient(CIO) {
                    engine {
                        maxConnectionsCount = 1000
                        threadsCount = Runtime.getRuntime().availableProcessors()
                        endpoint {
                            maxConnectionsPerRoute = 1000
                            pipelineMaxSize = 20
                            keepAliveTime = 10_000
                            connectTimeout = 10_000
                            connectAttempts = 5
                        }
                    }

                    install(HttpTimeout) {
                        requestTimeoutMillis = 10_000
                    }

                    install(ContentNegotiation) {
                        jackson {
                            registerModule(JavaTimeModule())
                            enable(SerializationFeature.INDENT_OUTPUT)
                        }
                    }
                }
                val scope = CoroutineScope(Dispatchers.IO)
                return EventSender(client, serverUrl, apiKey, scope)
            } catch (e: Exception) {
                throw EventSenderInitializationException("Failed to start EventSender: ${e.message}", e)
            }
        }
    }
}