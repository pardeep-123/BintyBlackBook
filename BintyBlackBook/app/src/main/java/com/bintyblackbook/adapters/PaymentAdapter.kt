package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.PaymentModel
import kotlinx.android.synthetic.main.item_payment.view.*

class PaymentAdapter(var context: Context, var arrayList: ArrayList<PaymentModel>) :
    RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCard:ImageView = itemView.ivCard
        val tvCardName:TextView = itemView.tvCardName
        val tvCardNo:TextView = itemView.tvCardNo
        val cb:CheckBox = itemView.cb

        fun bind(pos:Int){
            val paymentModel = arrayList[pos]
            ivCard.setImageResource(paymentModel.image!!)
            tvCardName.text = paymentModel.name
            tvCardNo.text = paymentModel.cardNo

            if (paymentModel.selected){
                cb.isChecked = paymentModel.selected
            }else{
                cb.isChecked = paymentModel.selected
            }

            cb.setOnClickListener {
                arrayList.forEachIndexed { index, paymentModel ->
                    if (index == pos){
                        paymentModel.selected = !paymentModel.selected
                    }else{
                        paymentModel.selected = false
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
}