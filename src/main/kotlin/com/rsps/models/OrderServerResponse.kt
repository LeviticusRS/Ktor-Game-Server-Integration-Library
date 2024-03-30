package com.rsps.models

data class OrderServerResponse(
    val total: Double,
    val details: List<OrderItem>
)