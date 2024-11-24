package com.idgs07.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.idgs07.firebase.databinding.ActivityAddUsersBinding
import com.idgs07.firebase.databinding.ActivityUpdateUserBinding

class UpdateUserActivity : AppCompatActivity() {
    lateinit var updateUserBinding: ActivityUpdateUserBinding

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = updateUserBinding.root
        enableEdgeToEdge()
        setContentView(view)
        getAndSetData()

        updateUserBinding.buttonUpdateUser.setOnClickListener{
            updateData()
        }
    }

    fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0).toString()
        val email = intent.getStringExtra("email")

        updateUserBinding.updateTextName.setText(name)
        updateUserBinding.updateTextAge.setText(age.toString())
        updateUserBinding.updateTextEmail.setText(email)

    }
    fun updateData (){
        val updatedName = updateUserBinding.updateTextName.text.toString()
        val updatedAge = updateUserBinding.updateTextAge.text.toString().toInt()
        val updatedEmail = updateUserBinding.updateTextEmail.text.toString()
        val userId = intent.getStringExtra("id").toString()

        val userMap = mutableMapOf<String,Any>()
        userMap ["userId"] = userId
        userMap ["userName"] = updatedName
        userMap ["userAge"] = updatedAge
        userMap ["userEmail"] = updatedEmail

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "the user has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}