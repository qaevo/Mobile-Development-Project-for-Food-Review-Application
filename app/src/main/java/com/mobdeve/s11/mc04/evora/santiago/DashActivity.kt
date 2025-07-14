package com.mobdeve.s11.mc04.evora.santiago

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashActivity : AppCompatActivity() {
    private lateinit var edTitle: EditText
    private lateinit var edBody: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnAcc: Button
    private lateinit var sqlHelper: SQLHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private var adapter: ReviewDataAdapter? = null
    private var rm: ReviewModel? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)
        initView()
        initRecyclerView()
        searchBar=findViewById(R.id.post_search)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val reviews = sqlHelper.searchPosts(it)
                }
                return true
            }
        })
        btnAcc = findViewById(R.id.accBtn)
        btnAcc.setOnClickListener{
            startActivity(Intent(this, AccActivity::class.java).apply {
            })
        }
        sqlHelper = SQLHelper(this)
        btnAdd.setOnClickListener { addPost() }
        btnView.setOnClickListener { getPosts()}
        btnUpdate.setOnClickListener { updatePost()}
        adapter?.setOnClickItem {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            edTitle.setText(it.title)
            edBody.setText(it.body)
            rm = it
        }

        adapter?.setOnClickDeleteItem {
            deletePost(it.id)
        }
    }

    private fun getPosts(){
        val stdList = sqlHelper.getAllPost()
        Log.e("pppp", "${stdList.size}")

        adapter?.addItems(stdList)
    }

    private fun addPost(){
        val title = edTitle.text.toString()
        val body= edBody.text.toString()

        if(body.isEmpty() || title.isEmpty()) {
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT
            ).show()
        }else {
            val std = ReviewModel(title = title, body = body)
            val status = sqlHelper.insertPost(std)
            if (status > -1) {
                Toast.makeText(this, "Post Added...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getPosts()
            }else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePost() {
        val title = edTitle.text.toString()
        val body = edBody.text.toString()
        if(title == rm?.title && body == rm?.body) {
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return
        }

        if(rm == null) return

        val std = ReviewModel(id = rm!!.id, title = title, body = body)
        val status = sqlHelper.updatePost(std)
        if (status > -1) {
            clearEditText()
            getPosts()
        }else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePost(id: Int) {
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, _->
            sqlHelper.deletePostById(id)
            getPosts()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, _->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edTitle.setText("")
        edBody.setText("")
        edTitle.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReviewDataAdapter(activity = Activity())
        val swipe = SwipeActivity(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipe.revAdapter = adapter
        itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edTitle = findViewById(R.id.edTitle)
        edBody = findViewById(R.id.edBody)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}
