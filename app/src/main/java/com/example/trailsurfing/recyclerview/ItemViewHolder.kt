package com.example.trailsurfing.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsurfing.BR

    class ItemViewHolder<T>(
        private val binding: ViewDataBinding,
        private val listener: OnItemClickListner<T>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            with(binding) {
                setVariable(BR.item, item)
                setVariable(BR.eventHandler, listener)
                executePendingBindings()
            }
        }
    }