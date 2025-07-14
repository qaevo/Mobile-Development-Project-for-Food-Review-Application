package com.mobdeve.s11.mc04.evora.santiago

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegActivity : AppCompatActivity() {
    private lateinit var uname: EditText
    private lateinit var name: EditText
    private lateinit var pword: EditText
    private lateinit var signupBtn: Button
    private lateinit var db: SQLHelper
    //private var um: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg        )
        uname = findViewById(R.id.username)
        name = findViewById(R.id.name)
        pword = findViewById(R.id.password)
        signupBtn = findViewById(R.id.regBtn2)
        db= SQLHelper(this)

        signupBtn.setOnClickListener{
            val unametext = uname.text.toString()
            val nametext = name.text.toString()
            val pwordtext = pword.text.toString()


            if(TextUtils.isEmpty(unametext) || TextUtils.isEmpty(nametext) || TextUtils.isEmpty(pwordtext)){
                Toast.makeText(this, "Add Username Name & Password", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                val um = UserModel(username=unametext,name=nametext, password = pwordtext)
                val savedata =db.insertUser(um)
                if(savedata > -1){
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Signup Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}
