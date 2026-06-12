package com.example.aplicacionnewbee.model

import com.example.aplicacionnewbee.R

data class Product(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val benefits: String = "",
    val imageRes: Int? = null
)

val sampleProducts = listOf(
    Product(1, "Miel Pura de Abeja", "Miel 100% natural de flores silvestres.", 15.0, "Energizante natural y refuerza defensas.", R.drawable.miel_pura),
    Product(2, "Propóleo", "Extracto puro de propóleo concentrado.", 10.0, "Antibiótico natural y antiinflamatorio.", R.drawable.propoleo),
    Product(3, "Jalea Real", "Sustancia nutritiva producida por abejas obreras.", 25.0, "Mejora el rendimiento físico y mental.", R.drawable.jalea_real),
    Product(4, "Polen de Flores", "Granos de polen recolectados por las abejas.", 12.0, "Excelente suplemento multivitamínico.", R.drawable.polen_flores),
    Product(5, "Jalea en Cápsulas", "Suplemento de jalea real en formato práctico.", 22.0, "Fácil de consumir para energía diaria.", R.drawable.jalea_capsulas),
    Product(6, "Propomiel", "Mezcla perfecta de miel con extracto de propóleo.", 16.0, "Ideal para aliviar dolores de garganta.", R.drawable.propomiel),
    Product(7, "Spray de Propóleo", "Spray bucal para protección inmediata.", 9.0, "Desinfecta y protege las vías respiratorias.", R.drawable.spray_propoleo)
)
