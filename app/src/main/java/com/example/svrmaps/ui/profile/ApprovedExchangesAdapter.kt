package com.example.svrmaps.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.svrmaps.databinding.ItemApprovedExchangeBinding
import com.example.svrmaps.databinding.ItemSubjectForOfferBinding
import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.ui.exchange.select_subject.SelectSubjectAdapter
import com.example.svrmaps.utils.visible

class ApprovedExchangesAdapter (
    private val clickListener: (Exchange) -> Unit
) : ListAdapter<Exchange, ApprovedExchangesAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Exchange>() {

        override fun areItemsTheSame(
            oldItem: Exchange,
            newItem: Exchange
        ): Boolean = oldItem.offerUserEmail == newItem.offerUserEmail

        override fun areContentsTheSame(
            oldItem: Exchange,
            newItem: Exchange
        ): Boolean = oldItem == newItem
    }
) {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemApprovedExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemApprovedExchangeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var data: Exchange

        init {
            itemBinding.btnCreateRoute.setOnClickListener {
                clickListener.invoke(data)
            }
        }

        fun bind(data: Exchange) {
            this.data = data
            itemBinding.subjectName.text = data.destSubjectName
            itemBinding.subjectUserName.text = data.destUserEmail
            itemBinding.yourSubjectName.text = data.offerUserEmail
        }
    }
}