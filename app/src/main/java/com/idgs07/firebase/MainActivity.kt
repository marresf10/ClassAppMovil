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

    lateinit var mainBinding: ActivityMainBinding

    // Firebase
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("MyUsers")
    private val userList = ArrayList<Users>()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //declarar toolbar
        setSupportActionBar(mainBinding.toolbar)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddUsersActivity::class.java)
            startActivity(intent)
        }

        retrievingDataFromDatabase()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)
                myReference.child(id).removeValue()
                Toast.makeText(applicationContext, "The user was deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(mainBinding.recyclerView)
    }

    private fun retrievingDataFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(Users::class.java)
                    if (user != null) userList.add(user)
                }
                usersAdapter = UsersAdapter(this@MainActivity, userList)
                mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                mainBinding.recyclerView.adapter = usersAdapter
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
        if (item.itemId == R.id.deleteAll) {
            showDialogMessage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogMessage() {
        AlertDialog.Builder(this)
            .setTitle("Delete all users")
            .setMessage("If you click yes, all users will be deleted. If you want to delete a specific user, swipe the item.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                myReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        usersAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "All users deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .create()
            .show()
    }

}


