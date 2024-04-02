package com.example.harshamoduleproject

data class Booking(
    val slNo: Int,
    val roomType: String,
    val roomNumber: String,
    val checkInDate: String,
    val checkOutDate: String,
    val price: CharSequence?,
    val status: String
)
