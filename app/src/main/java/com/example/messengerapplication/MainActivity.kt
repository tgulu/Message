package com.example.messengerapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messengerapplication.adapter.UserAdapter
import com.example.messengerapplication.databinding.ActivityMainBinding
import com.example.messengerapplication.model.User
import com.example.messengerapplication.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userList: ArrayList<User> = ArrayList()
    private lateinit var userAdapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialsViews()
    }

    private fun initialsViews() {
        mAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        userAdapter = UserAdapter(this, userList)
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        mDatabaseRef.child("user").addValueEventListener(@SuppressLint("NotifyDataSetChanged")
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (post in snapshot.children) {
                    val user = post.getValue(User::class.java)

                    if (mAuth.currentUser?.uid != user?.uniqueIdentifier) {
                        userList.add(user!!)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent= Intent(this@MainActivity, LoginActivity::class.java)
            finish()
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            return true
        }
        return true

    }
}