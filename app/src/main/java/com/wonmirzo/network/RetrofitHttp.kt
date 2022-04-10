package com.wonmirzo.network

import com.wonmirzo.network.services.CardService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {
    const val IS_TESTER = true
    private const val SERVER_DEVELOPMENT = "https://6232ae668364d63035c1809f.mockapi.io/"
    private const val SERVER_PRODUCTION = "https://6232ae668364d63035c1809f.mockapi.io/"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(server())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun server(): String {
        if (IS_TESTER) return SERVER_DEVELOPMENT
        return SERVER_PRODUCTION
    }

    val cardService: CardService = retrofit.create(CardService::class.java)
    //...
}