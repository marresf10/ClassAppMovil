package com.idgs07.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idgs07.firebase.databinding.ActivityAddUsersBinding

class AddUsersActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUsersBinding

    //Retrieving
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        addUserBinding = ActivityAddUsersBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_add_users)
        setContentView(view)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         */
        supportActionBar?.title = "Add user"

        addUserBinding.buttonAddUsers.setOnClickListener {
            if (validateFields()) {
                addUsersToDatabase()
            }
        }
    }

    fun validateFields(): Boolean {
        val name = addUserBinding.editTextName.text.toString()
        val ageText = addUserBinding.editTextAge.text.toString()
        val email = addUserBinding.editTextEmail.text.toString()

        return when {
            name.isEmpty() -> {
                addUserBinding.editTextName.error = "Name is required"
                false
            }
            ageText.isEmpty() -> {
                addUserBinding.editTextAge.error = "Age is required"
                false
            }
            email.isEmpty() -> {
                addUserBinding.editTextEmail.error = "Email is required"
                false
            }
            else -> true
        }
    }

    fun addUsersToDatabase() {
        val name: String = addUserBinding.editTextName.text.toString()
        val age: Int = addUserBinding.editTextAge.text.toString().toInt()
        val email: String = addUserBinding.editTextEmail.text.toString()

        val id: String = myReference.push().key.toString()
        val user = Users(id, name, age, email)

        myReference.child(id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "The new user has been added to the database",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {
                Toast.makeText(
                    applicationContext, task.exception.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }

    }



}