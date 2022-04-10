package com.wonmirzo.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.wonmirzo.R
import com.wonmirzo.network.RetrofitHttp
import com.wonmirzo.network.model.Card
import com.wonmirzo.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Card Activity for add cards
* */
class CardActivity : AppCompatActivity() {
    private val tag = CardActivity::class.simpleName

    private lateinit var etCardNumber: EditText
    private lateinit var etExDateMonth: EditText
    private lateinit var etExDateYear: EditText
    private lateinit var etCvv: EditText
    private lateinit var etCardHolderName: EditText
    private lateinit var btnAddCard: Button

    private lateinit var tvCardNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_card)
        initViews()
    }

    private fun initViews() {
        tvCardNumber = findViewById(R.id.tvCardNumber)
        etCardNumber = findViewById(R.id.etCardNumber)
        etExDateMonth = findViewById(R.id.etExDateMonth)
        etExDateYear = findViewById(R.id.etExDateYear)
        etCvv = findViewById(R.id.etCvv)
        etCardHolderName = findViewById(R.id.etCardHolderName)
        btnAddCard = findViewById(R.id.btnAddCard)
        val ivCancel: ImageView = findViewById(R.id.ivCancel)
        ivCancel.setOnClickListener {
            finish()
        }

        etCardNumber.addTextChangedListener(etCardNumberWatcher)
        btnAddCard.setOnClickListener {
            val cardNumber = etCardNumber.text.toString()
            val cardHolderName = etCardHolderName.text.toString()
            val expiresDate = etExDateMonth.text.toString() + etExDateYear.text.toString()
            if (checkFilled(cardNumber, cardHolderName, expiresDate)) {
                val card = Card(cardNumber, cardHolderName, expiresDate)
                sendData(card)
                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill empty fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val etCardNumberWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0!!.length < 16) {
                tvCardNumber.text = p0
            } else {
                etCardNumber.isEnabled = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0!!.length <= 16) {
                tvCardNumber.text = p0
            }
        }
    }

    private fun checkFilled(
        cardNumber: String,
        cardHolderName: String,
        expiresDate: String
    ): Boolean {
        if (cardNumber.isEmpty()) {
            etCardNumber.error = "Please fill the field"
            return false
        }
        if (cardHolderName.isEmpty()) {
            etCardHolderName.error = "Please fill the field"
            return false
        }
        return true
    }

    private fun sendData(card: Card) {
        saveDataOnServer(card)
        Intent(this@CardActivity, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun saveDataOnServer(card: Card) {
        RetrofitHttp.cardService.createCard(card).enqueue(object : Callback<Card> {
            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (!response.isSuccessful) {
                    Logger.d(tag!!, "Error Code: ${response.code()}")
                    return
                }

                Logger.d(tag!!, "Card created: ${response.toString()}")
            }

            override fun onFailure(call: Call<Card>, t: Throwable) {
                Logger.e(tag!!, "Error: ${t.message}")
            }
        })
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