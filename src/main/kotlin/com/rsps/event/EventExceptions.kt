package com.rsps.event

import com.rsps.EventSender

/**
 * Custom exception thrown when there is a failure to send an event using [EventSender].
 *
 * @param message A detailed message describing the cause of the exception.
 * @param cause The underlying cause of the exception (optional).
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
class SendEventException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * Custom exception thrown when there is a failure during the initialization of [EventSender].
 *
 * @param message A detailed message describing the cause of the exception.
 * @param cause The underlying cause of the exception (optional).
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
class EventSenderInitializationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)