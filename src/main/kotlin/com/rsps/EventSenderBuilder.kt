package com.rsps

import com.rsps.EventSender.Companion.init

/**
 * A builder class for creating an instance of the [EventSender] class.
 * This builder simplifies the construction of an [EventSender] object by allowing you
 * to set the server URL and API key.
 */
class EventSenderBuilder {
    private var serverUrl: String? = null
    private var apiKey: String? = null

    /**
     * Sets the server URL to be used by the [EventSender].
     *
     * @param serverUrl The URL of the server where events will be sent.
     * @return This [EventSenderBuilder] instance, allowing for method chaining.
     */
    fun withServerUrl(serverUrl: String?): EventSenderBuilder {
        this.serverUrl = serverUrl
        return this
    }

    /**
     * Sets the API key to be used by the [EventSender].
     *
     * @param apiKey The API key for authentication when sending events.
     * @return This [EventSenderBuilder] instance, allowing for method chaining.
     */
    fun withApiKey(apiKey: String?): EventSenderBuilder {
        this.apiKey = apiKey
        return this
    }

    /**
     * Builds and returns an [EventSender] instance with the specified server URL and API key.
     *
     * @return An [EventSender] instance configured with the provided server URL and API key.
     * @throws IllegalStateException if the server URL or API key is not set.
     */
    fun build(): EventSender {
        requireNotNull(serverUrl) { "Server URL must be set" }
        requireNotNull(apiKey) { "API key must be set" }
        return init(serverUrl!!, apiKey!!)
    }
}
