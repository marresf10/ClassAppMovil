package com.idgs07.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.idgs07.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding

    //Retrieving
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    //Retrieving data 2
    val userList = ArrayList<Users>()
    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = mainBinding.root
        setContentView(view)
        enableEdgeToEdge()

        mainBinding.floatingActionButton.setOnClickListener{
            val intent  = Intent (this, AddUsersActivity::class.java)
            startActivity(intent)
        }

        retrievingDataFromDatabase()
    }

    fun retrievingDataFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(Users::class.java)
                    if (user != null) {
                        println("UserId: ${user.userId}")
                        println("UserId: ${user.userName}")
                        println("UserId: ${user.userAge}")
                        println("UserId: ${user.userEmail}")
                        println("****************************")

                        //Class 2
                        userList.add(user)
                    }

                    //Class2
                    usersAdapter = UsersAdapter(this@MainActivity, userList)
                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recyclerView.adapter = usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
            }
        })
    }

}


