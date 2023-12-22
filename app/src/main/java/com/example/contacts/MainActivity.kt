package com.example.contacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import com.example.contacts.model.Contact

class MainActivity : AppCompatActivity(), ContactAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val contactViewModel: ContactViewModel by viewModels {
        ContactViewModel.ContactViewModelFactory((application as ContactApplication).repository)
    }
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerview
        adapter = ContactAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactViewModel.allContacts.observe(this) { contact ->
            contact?.let { adapter.submitList(it) }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddContact::class.java)
            intent.putExtra("type", "addMode")
            startActivityForResult(intent, 1)
        }
    }

    override fun onEditClick(position: Int) {
        val intent = Intent(this@MainActivity, AddContact::class.java)
        intent.putExtra("type", "update")
        val contact = adapter.getContact(position)
        intent.putExtra("name", contact.name)
        intent.putExtra("number", contact.number)
        intent.putExtra("id", contact.id)
        startActivityForResult(intent, 2)
    }

    override fun onDeleteClick(position: Int) {
        val contact = adapter.getContact(position)
        contactViewModel.delete(contact)
        Toast.makeText(applicationContext, "Contact Deleted", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val reply1 = data?.getStringExtra(AddContact.EXTRA_REPLY)
            val reply2 = data?.getStringExtra(AddContact.EXTRA_REPLY2)

            if (reply1 != null && reply2 != null) {
                val todo = Contact(0, reply1, reply2)
                contactViewModel.insert(todo)
                Toast.makeText(applicationContext, "Contact Added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (requestCode == 2) {
            val reply1 = data?.getStringExtra(AddContact.EXTRA_REPLY)
            val reply2 = data?.getStringExtra(AddContact.EXTRA_REPLY2)

            if (reply1 != null && reply2 != null) {
                val id = data.getIntExtra("id", 0)
                val todo = Contact(id, reply1, reply2)
                contactViewModel.update(todo)
                Toast.makeText(applicationContext, "Contact Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }

    }

}