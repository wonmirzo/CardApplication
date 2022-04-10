package com.wonmirzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wonmirzo.R
import com.wonmirzo.network.model.Card

class CardAdapter(private var cards: ArrayList<Card>) :
    RecyclerView.Adapter<CardAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardAdapter.VH, position: Int) {
        val card = cards[position]

        val tvCardNumber = holder.tvCardNumber
        val tvCardHolderName = holder.tvCardHolderName
        val tvExpiresDate = holder.tvExpiresDate

        tvCardNumber.text = card.cardNumber
        tvCardHolderName.text = card.cardHolderName
        tvExpiresDate.text = card.expiresDate
    }

    override fun getItemCount(): Int = cards.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvCardNumber: TextView = view.findViewById(R.id.tvCardNumber)
        val tvCardHolderName: TextView = view.findViewById(R.id.tvCardHolderName)
        val tvExpiresDate: TextView = view.findViewById(R.id.tvExpiresDate)
    }
}