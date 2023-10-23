package com.rsps.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * Base event class.
 *
 * @property timestamp The timestamp when the event occurred.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PublicMessageEvent::class, name = "publicMessage"),
    JsonSubTypes.Type(value = ClanChatEvent::class, name = "clanChat"),
    JsonSubTypes.Type(value = PlayerSnapshotEvent::class, name = "playerSnapshot"),
    JsonSubTypes.Type(value = NewPlayerEvent::class, name = "newPlayer"),
    JsonSubTypes.Type(value = TradeEvent::class, name = "trade")
)
open class Event(open val timestamp: Date = Date.from(LocalDateTime.now(ZoneId.of("America/New_York")).atZone(ZoneId.of("America/New_York")).toInstant()))

/**
 * Base event class for events associated with a player.
 *
 * @property username The username associated with the event.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
open class PlayerBasedEvent(
    open val username: String
) : Event()

/**
 * Base event class for events associated with a position.
 *
 * @property x The x coordinate associated with the event.
 * @property y The y coordinate associated with the event.
 * @property z The z coordinate associated with the event.
 *
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
open class CoordinatedEvent(
    open var x: Int = 0,
    open var y: Int = 0,
    open var z: Int = 0
) : Event() {
    fun setPosition(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }
}

/**
 * Base event class for events associated with a position and player.
 *
 * @property x The x coordinate associated with the event.
 * @property y The y coordinate associated with the event.
 * @property z The z coordinate associated with the event.
 *
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
open class PlayerBasedCoordinatedEvent(
    open var x: Int = 0,
    open var y: Int = 0,
    open var z: Int = 0,
    override val username: String
) : PlayerBasedEvent(username) {
    fun setPosition(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }
}

/**
 * Event representing a public message, sent in the game.
 *
 * @property message The content of the public message.
 * @param username The username of the player sending the message.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
data class PublicMessageEvent(
    val message: String,
    override val username: String
) : PlayerBasedCoordinatedEvent(username = username)

/**
 * Event representing a clan chat message, sent by any clan-chat
 *
 * @property clan The clan associated with the chat message.
 * @param username The username of the player sending the message.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
data class ClanChatEvent(
    val clan: String,
    val message: String,
    override val username: String
) : PlayerBasedCoordinatedEvent(username = username)

/**
 * Represents an event for saving player data.
 *
 * @property snapshot JSON representation of the player's data snapshot.
 * @property username The username of the player associated with the event.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
data class PlayerSnapshotEvent(
    val snapshot: String,
    override val username: String
) : PlayerBasedEvent(username)

/**
 * Event representing a new player has joined the game.
 *
 * @property username The username associated with the event.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
open class NewPlayerEvent(
    override val username: String
) : PlayerBasedEvent(username)

/**
 * Event representing a trade event, between two players
 *
 * @property traderUsername The username of the player sending the trade.
 * @property receiverUsername The username of the player receiver the trade.
 * @author Joshua Ransom (<a href="https://github.com/LeviticusRS">LeviticusRS</a>)
 */
data class TradeEvent(
    val traderUsername: String,
    val receiverUsername: String,
    val itemsGameJson: String,
    val itemsReceivedJson: String
) : CoordinatedEvent()