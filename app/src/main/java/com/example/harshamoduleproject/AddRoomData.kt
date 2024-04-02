package com.example.harshamoduleproject

data class AddRoomData(
    val userId: String? = null,
    val roomtype: String? = null,
    val price: String? = null,
    val totalrooms: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val date: String? = null,
    val status: String? = "pending",
    val myId: String? = null,

    val adult: String? = null,
    val child: String? = null,
    val checkIn: String? = null,
    val checkOut: String? = null,
    var roomno: Int? = null,
    var days: Int? = null
)

