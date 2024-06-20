package com.appadore.test.ui.question.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appadore.test.databinding.ItemOptionBinding
import com.appadore.test.domain.model.Country

class CountryAdapter(
    private val listener: OnClickListener
) : ListAdapter<Country, RecyclerView.ViewHolder>(CountryDiffCallback) {

    var answerId: Int? = null
    var selectedId: Int? = null

    interface OnClickListener {
        fun onCountryClicked(country: Country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CountryViewHolder).bind(getItem(position))
    }

    inner class CountryViewHolder(private val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.tvCountryName.text = country.countryName

            binding.cardContent.setOnClickListener {
                listener.onCountryClicked(country)
            }
        }
    }
}

object CountryDiffCallback: DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }

}