package com.idgs07.firebase

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("NOT YET IMPLEMENTED")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //viewHolder.adapterPosition

                val id = usersAdapter.getUserId(viewHolder.adapterPosition)
                myReference.child(id).removeValue()
                Toast.makeText(applicationContext, "The user was deleted",
                    Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(mainBinding.recyclerView)
        //Finished
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_all, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll){
            showDialogMessage()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete all users")
        dialogMessage.setMessage(
            "If you click yes, all users will be deleted, " +
                    "if you want to delete a specific user swipe the item you want to delete"
        )
        dialogMessage.setNegativeButton("Cancel") { dialogInterface, i ->

        }
        dialogMessage.setPositiveButton("Yes") { dialogInterface, i ->
            myReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    usersAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "All users deleted successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialogMessage.create().show()
    }


}


