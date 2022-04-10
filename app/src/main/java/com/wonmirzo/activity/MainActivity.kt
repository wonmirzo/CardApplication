package com.wonmirzo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wonmirzo.R
import com.wonmirzo.adapter.CardAdapter
import com.wonmirzo.network.RetrofitHttp
import com.wonmirzo.network.model.Card
import com.wonmirzo.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
*  MainActivity as home page
* */
class MainActivity : AppCompatActivity() {
    private val tag = MainActivity::class.simpleName

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivAdd: ImageView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var cards: ArrayList<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        cards = ArrayList()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        ivAdd = findViewById(R.id.ivAdd)
        ivAdd.setOnClickListener {
            openAddPage()
        }
        apiCardList()
        refreshAdapter(cards)
    }

    private fun refreshAdapter(cards: ArrayList<Card>) {
        cardAdapter = CardAdapter(cards)
        recyclerView.adapter = cardAdapter
    }


    private fun apiCardList() {
        RetrofitHttp.cardService.getAllCards().enqueue(object : Callback<ArrayList<Card>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Card>>,
                response: Response<ArrayList<Card>>
            ) {
                if (!response.isSuccessful) {
                    Logger.d(tag!!, "Error Code: ${response.code()}")
                    return
                }

                cards.addAll(response.body()!!)
                cardAdapter.notifyDataSetChanged()
                Logger.d(tag!!, "Data size: ${cards.size}")
            }

            override fun onFailure(call: Call<ArrayList<Card>>, t: Throwable) {
                Logger.e(tag!!, "Error: ${t.message}")
            }
        })
    }


    private fun openAddPage() {
        Intent(this@MainActivity, CardActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
