package com.wonmirzo.network.services

import com.wonmirzo.network.model.Card
import retrofit2.Call
import retrofit2.http.*

interface CardService {

    @GET("cards")
    fun getAllCards(): Call<ArrayList<Card>>

    @GET("cards{id}")
    fun getOneCard(@Path("id") id: Int): Call<Card>

    @POST("cards")
    fun createCard(@Body card: Card): Call<Card>

    @PUT("cards/{id}")
    fun updateCard(@Path("id") id: Int, @Body card: Card): Call<Card>

    @DELETE("cards/{id}")
    fun deleteCard(@Path("id") id: Int): Call<Card>
}