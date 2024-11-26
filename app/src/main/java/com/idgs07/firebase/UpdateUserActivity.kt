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

    fun updateData() {
        val updatedName = updateUserBinding.updateTextName.text.toString().trim()
        val updatedAgeText = updateUserBinding.updateTextAge.text.toString().trim()
        val updatedEmail = updateUserBinding.updateTextEmail.text.toString().trim()

        if (updatedName.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }
        if (updatedAgeText.isEmpty()) {
            Toast.makeText(this, "La edad no puede estar vacía", Toast.LENGTH_SHORT).show()
            return
        }
        if (updatedEmail.isEmpty()) {
            Toast.makeText(this, "El correo electrónico no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedAge = try {
            updatedAgeText.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Por favor ingresa un número válido para la edad", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = intent.getStringExtra("id").toString()

        val userMap = mutableMapOf<String, Any>()
        userMap["userId"] = userId
        userMap["userName"] = updatedName
        userMap["userAge"] = updatedAge
        userMap["userEmail"] = updatedEmail

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "El usuario ha sido actualizado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

}