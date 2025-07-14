package com.mobdeve.s11.mc04.evora.santiago

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView.FindListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.mc04.evora.santiago.R

class AccActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var db: SQLHelper
    private lateinit var deleteBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var sqlHelper: SQLHelper
    private var um: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acc)
        deleteBtn = findViewById(R.id.deleteBtn)
        saveBtn = findViewById(R.id.saveBtn)
        editName= findViewById(R.id.editName)
        db = SQLHelper(this)
        deleteBtn.setOnClickListener {
            um?.username?.let { username ->
                deleteUser(username)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        saveBtn.setOnClickListener{updateName()}
    }

    private fun updateName() {
        val name = editName.text.toString()
        if(name == um?.name) {
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return
        }
        if(um == null) return

        val um = UserModel(username = um!!.username, name = name)
        val status = sqlHelper.updateUser(um)
        if (status > -1) {
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteUser(username: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqlHelper.deleteUser(username)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

}