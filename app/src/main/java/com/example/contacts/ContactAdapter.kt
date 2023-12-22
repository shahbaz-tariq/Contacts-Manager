package com.example.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.model.Contact

class ContactAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactComparator()) {

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun getContact(position: Int): Contact {
        return getItem(position)
    }

    class ContactViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tvName)
        private val number: TextView = itemView.findViewById(R.id.tvNumber)
        private val editImageView: ImageView = itemView.findViewById(R.id.ivEdit)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)

        fun bind(contact: Contact) {
            name.text = contact.name
            number.text = contact.number

            editImageView.setOnClickListener {
                listener.onEditClick(adapterPosition)
            }

            deleteImageView.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: OnItemClickListener): ContactViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.each_rv, parent, false)
                return ContactViewHolder(view, listener)
            }
        }
    }

    class ContactComparator : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
