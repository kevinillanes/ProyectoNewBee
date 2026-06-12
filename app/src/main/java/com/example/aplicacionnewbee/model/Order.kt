package com.example.aplicacionnewbee.model

data class Order(
    val id: String = "",
    val items: List<Product> = emptyList(),
    val total: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pendiente"
)
