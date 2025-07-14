package com.mobdeve.s11.mc04.evora.santiago

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class SQLHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "post.db"
        private const val REVIEW_TBL = "review_tbl"
        private const val USER_TBL = "user_tbl"
        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val PASS = "password"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val BODY = "body"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTbl = ("CREATE TABLE " + USER_TBL + "(" + USERNAME + " TEXT PRIMARY KEY," + NAME + " TEXT," + PASS + " TEXT" + ")")
        val createRevTbl = ("CREATE TABLE " + REVIEW_TBL + "(" + USERNAME + " TEXT PRIMARY KEY," + TITLE + " TEXT," + ID + " INTEGER," + BODY + " TEXT" + ")")
        db?.execSQL(createUserTbl)
        db?.execSQL(createRevTbl)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $USER_TBL")
        db!!.execSQL("DROP TABLE IF EXISTS $REVIEW_TBL")
        onCreate(db)
    }

    fun updateUser(um: UserModel): Int {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $USERNAME FROM $USER_TBL WHERE username = ?", arrayOf("1"))

        val username = cursor.moveToFirst()
        val contentValues = ContentValues()
        contentValues.put(NAME, um.name)

        val success = db.update(USER_TBL, contentValues, "$USERNAME = " + username, null)
        cursor.close()
        db.close()
        return success
    }
    fun deleteUser(username: String): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, username)

        val success = db.delete(USER_TBL, "username=$username", null)
        db.close()
        return success
    }
    fun searchPosts(query: String): List<ReviewModel> {
        val db = this.readableDatabase
        val cursor = db.query(
            REVIEW_TBL,
            arrayOf(ID, TITLE, BODY),
            "$TITLE LIKE ? OR $BODY LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null,
            null,
            null
        )
        val reviews = mutableListOf<ReviewModel>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val body = getString(getColumnIndexOrThrow(BODY))
                val review = ReviewModel(id = id, title = title, body = body)
                reviews.add(review)
            }
        }
        cursor.close()
        db.close()
        return reviews
    }

    fun insertUser(um: UserModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, um.username)
        contentValues.put(NAME, um.name)
        contentValues.put(PASS, um.password)

        val success = db.insert(USER_TBL, null, contentValues)
        db.close()
        return success
    }

    fun checkUserPass(um: UserModel): Boolean {
        val db = this.writableDatabase
        val username = um.username
        val password = um.password
        val query = "SELECT $USERNAME, $PASS FROM $USER_TBL WHERE $USERNAME= '$username' and $PASS= '$password'"
        val cursor = db.rawQuery(query,null)
        if(cursor.count<=0){
            cursor.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }

    fun insertPost(rm: ReviewModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, rm.id)
        contentValues.put(TITLE, rm.title)
        contentValues.put(BODY, rm.body)

        val success = db.insertWithOnConflict(REVIEW_TBL, null, contentValues,1)
        db.close()
        return success
    }

    fun getAllPost(): ArrayList<ReviewModel> {
        val stdList: ArrayList<ReviewModel> = ArrayList()
        val selectQuery = "SELECT * FROM $REVIEW_TBL"
        val db = this.readableDatabase

        val cursor: Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var body: String

        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                body = cursor.getString(cursor.getColumnIndexOrThrow(BODY))

                val rm = ReviewModel(id = id, title = title, body = body)
                stdList.add(rm)
            }
        }
        return stdList
    }

    fun updatePost(rm: ReviewModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, rm.id)
        contentValues.put(TITLE, rm.title)
        contentValues.put(BODY, rm.body)

        val success = db.update(REVIEW_TBL, contentValues, "id=" + rm.id, null)
        db.close()
        return success
    }

    fun deletePostById(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(REVIEW_TBL, "id=$id", null)
        db.close()
        return success
    }
}
