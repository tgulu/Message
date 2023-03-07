package com.example.messengerapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.messengerapplication.MainActivity
import com.example.messengerapplication.databinding.ActivitySignUpBinding
import com.example.messengerapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mDataBase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialsViews()
    }

    //take username and password details from text views
    private fun initialsViews() {
        mAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        binding.btnSignUp.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val name = binding.editName.text.toString().trim().capitalize()
            val password = binding.editPassword.text.toString().trim()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signUp(name, email, password)
            } else {
                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //sign up and add user to database
    private fun signUp(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }

    }

    //for adding user to the database
    private fun addUserToDataBase(name: String, email: String, uid: String){
        mDataBase = FirebaseDatabase.getInstance().reference
        mDataBase.child("user").child(uid).setValue((User(name,email, uid)))


    }
}