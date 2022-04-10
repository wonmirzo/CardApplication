package com.wonmirzo.network.model

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("card_holder_name")
    val cardHolderName: String,
    @SerializedName("expires_date")
    val expiresDate: String,
    val id: Int? = null
)
