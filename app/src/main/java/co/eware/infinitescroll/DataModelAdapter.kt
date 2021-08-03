package co.eware.infinitescroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.eware.infinitescroll.databinding.ItemDataListLayoutBinding


class DataModelAdapter : ListAdapter<Person, DataModelAdapter.PersonViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemDataListLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val item: Person = getItem(position)
        holder.bind(item)
    }

    inner class PersonViewHolder(
        private val binding: ItemDataListLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Person) {
            binding.tvName.text = item.name
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}