package com.example.aplicacionnewbee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.aplicacionnewbee.model.FirebaseManager
import com.example.aplicacionnewbee.model.Product
import com.example.aplicacionnewbee.ui.screens.*
import com.example.aplicacionnewbee.ui.theme.AplicacionNewbeeTheme
import kotlinx.coroutines.launch

enum class Screen {
    Login, Register, Catalog, ProductDetail, Cart, Favorites, Orders
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            var currentScreen by rememberSaveable { mutableStateOf(Screen.Login) }
            var selectedProduct by rememberSaveable { mutableStateOf<Product?>(null) }
            
            // Listas para el estado global de la app
            val cartItems = remember { mutableStateListOf<Product>() }
            val favoriteItems = remember { mutableStateListOf<Product>() }
            
            AplicacionNewbeeTheme {
                // Manejo del botón atrás físico de Android
                BackHandler(enabled = currentScreen != Screen.Login && currentScreen != Screen.Catalog) {
                    currentScreen = when (currentScreen) {
                        Screen.Register -> Screen.Login
                        else -> Screen.Catalog
                    }
                }

                when (currentScreen) {
                    Screen.Login -> LoginScreen(
                        onLoginSuccess = { currentScreen = Screen.Catalog },
                        onNavigateToRegister = { currentScreen = Screen.Register }
                    )
                    Screen.Register -> RegisterScreen(
                        onRegisterSuccess = { currentScreen = Screen.Login },
                        onNavigateToLogin = { currentScreen = Screen.Login }
                    )
                    Screen.Catalog -> CatalogScreen(
                        onProductClick = { product ->
                            selectedProduct = product
                            currentScreen = Screen.ProductDetail
                        },
                        onNavigateToCart = { currentScreen = Screen.Cart },
                        onNavigateToFavorites = { currentScreen = Screen.Favorites },
                        onNavigateToOrders = { currentScreen = Screen.Orders },
                        onFavoriteClick = { product ->
                            scope.launch {
                                if (favoriteItems.any { it.id == product.id }) {
                                    FirebaseManager.removeFromFavorites(product)
                                    favoriteItems.removeAll { it.id == product.id }
                                } else {
                                    FirebaseManager.addToFavorites(product)
                                    favoriteItems.add(product)
                                }
                            }
                        },
                        favoriteItems = favoriteItems
                    )
                    Screen.ProductDetail -> selectedProduct?.let { product ->
                        ProductDetailScreen(
                            product = product,
                            onBack = { currentScreen = Screen.Catalog },
                            onAddToCart = { 
                                scope.launch {
                                    FirebaseManager.addToCart(product)
                                    cartItems.add(product)
                                    currentScreen = Screen.Cart
                                }
                            }
                        )
                    }
                    Screen.Cart -> CartScreen(
                        cartItems = cartItems,
                        onBack = { currentScreen = Screen.Catalog },
                        onRemoveItem = { 
                            scope.launch {
                                FirebaseManager.removeFromCart(it)
                                cartItems.remove(it)
                            }
                        },
                        onCheckout = { 
                            scope.launch {
                                val total = cartItems.sumOf { it.price }
                                FirebaseManager.createOrder(cartItems.toList(), total)
                                cartItems.clear()
                                currentScreen = Screen.Orders 
                            }
                        }
                    )
                    Screen.Favorites -> FavoritesScreen(
                        favoriteItems = favoriteItems,
                        onBack = { currentScreen = Screen.Catalog },
                        onRemoveFavorite = { favoriteItems.remove(it) }
                    )
                    Screen.Orders -> OrdersScreen(
                        onBack = { currentScreen = Screen.Catalog }
                    )
                }
            }
        }
    }
}
