package com.example.svrmaps.ui.exchange.select_subject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.svrmaps.databinding.ItemSubjectForOfferBinding
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.utils.visible

class SelectSubjectAdapter (
    private val clickListener: (Subject) -> Unit
) : ListAdapter<Subject, SelectSubjectAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Subject>() {

        override fun areItemsTheSame(
            oldItem: Subject,
            newItem: Subject
        ): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: Subject,
            newItem: Subject
        ): Boolean = oldItem == newItem
    }
) {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSubjectForOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemSubjectForOfferBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var data: Subject

        init {
            itemBinding.root.setOnClickListener {
                clickListener.invoke(data)
            }
        }

        fun bind(data: Subject) {
            this.data = data
            itemBinding.tvSubject.text = data.name
            itemBinding.checkSubjectView.visible(adapterPosition == selectedPosition)
        }
    }
}