package com.mobdeve.s11.mc04.evora.santiago

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private lateinit var uname : EditText
    private lateinit var pword : EditText
    private lateinit var db: SQLHelper
    //private var um: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val regBtn = findViewById<Button>(R.id.regBtn)
        loginBtn= findViewById(R.id.loginBtn)
        uname = findViewById(R.id.userID)
        pword = findViewById(R.id.passID)
        db = SQLHelper(this)
        regBtn.setOnClickListener{
            startActivity(Intent(this, RegActivity::class.java).apply {
            })

        }
        loginBtn.setOnClickListener{
            val usertext = uname.text.toString()
            val passtext = pword.text.toString()

            if(TextUtils.isEmpty(usertext) || TextUtils.isEmpty(passtext)){
                Toast.makeText(this, "Add Username, Password", Toast.LENGTH_SHORT).show()
            }
            else{
                val um1 = UserModel (username=usertext, password = passtext)
                val checkuser = db.checkUserPass(um1)
                if(checkuser == true){
                    Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, DashActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Wrong Username/Password", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }




}
