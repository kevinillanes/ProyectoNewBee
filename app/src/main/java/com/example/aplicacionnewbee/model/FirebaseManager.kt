package com.example.aplicacionnewbee.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Guardar perfil de usuario
    suspend fun saveUser(name: String, email: String) {
        val userId = getCurrentUserId() ?: return
        val userData = mapOf("name" to name, "email" to email)
        db.child("users").child(userId).child("profile").setValue(userData).await()
    }

    // Gestión del Carrito en Realtime Database
    suspend fun addToCart(product: Product) {
        val userId = getCurrentUserId() ?: return
        db.child("users").child(userId).child("cart")
            .child(product.id.toString()).setValue(product).await()
    }

    suspend fun removeFromCart(product: Product) {
        val userId = getCurrentUserId() ?: return
        db.child("users").child(userId).child("cart")
            .child(product.id.toString()).removeValue().await()
    }

    // Gestión de Favoritos en Realtime Database
    suspend fun addToFavorites(product: Product) {
        val userId = getCurrentUserId() ?: return
        db.child("users").child(userId).child("favorites")
            .child(product.id.toString()).setValue(product).await()
    }

    suspend fun removeFromFavorites(product: Product) {
        val userId = getCurrentUserId() ?: return
        db.child("users").child(userId).child("favorites")
            .child(product.id.toString()).removeValue().await()
    }

    // Gestión de Pedidos
    suspend fun createOrder(items: List<Product>, total: Double) {
        val userId = getCurrentUserId() ?: return
        val orderRef = db.child("users").child(userId).child("orders").push()
        val orderId = orderRef.key ?: return
        val order = Order(id = orderId, items = items, total = total)
        orderRef.setValue(order).await()
        
        // Vaciar carrito después del pedido
        db.child("users").child(userId).child("cart").removeValue().await()
    }

    suspend fun getOrders(): List<Order> {
        val userId = getCurrentUserId() ?: return emptyList()
        val snapshot = db.child("users").child(userId).child("orders").get().await()
        return snapshot.children.mapNotNull { it.getValue(Order::class.java) }
    }
}
