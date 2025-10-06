package com.example.accesapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.accesapp.dataBase.DBHelper
import com.example.accesapp.model.User
import kotlin.String

class UserDao(context: Context){
    private val dbHelper = DBHelper(context)

    fun insert(user: User):Boolean{
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("rut", user.rut)
            put("nombreUsuario", user.nombreUsuario)
            put("nombre", user.nombre)
            put("apellidoP", user.apellidoP)
            put("apellidoM", user.apellidoM)
            put("correo", user.correo)
            put("contrasena", user.contrasena)
            put("genero", user.genero)
        }
        val id = db.insert("users", null, value)
        db.close()
        return id != -1L
    }

    fun update(user: User):Boolean{
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("nombreUsuario", user.nombreUsuario)
            put("nombre", user.nombre)
            put("apellidoP", user.apellidoP)
            put("apellidoM", user.apellidoM)
            put("correo", user.correo)
            put("contrasena", user.contrasena)
            put("genero", user.genero)
        }
        val rows = db.update("users", value, "rut = ?", arrayOf(user.rut))
        db.close()
        return rows > 0
    }

    fun delete(rut: String):Boolean{
        val db = dbHelper.writableDatabase
        val rows = db.delete("users", "rut = ?", arrayOf(rut))
        db.close()
        return rows > 0
    }

    fun getAll(): List<User>{
        val db = dbHelper.readableDatabase
        val list = mutableListOf<User>()

        val c: Cursor = db.rawQuery("SELECT * FROM users ORDER BY apellidoP", null)
        c.use {
            while (it.moveToNext()) {
                list.add(
                    User(
                        rut = it.getString(0),
                        nombreUsuario = it.getString(1),
                        nombre = it.getString(2),
                        apellidoP = it.getString(3),
                        apellidoM = it.getString(4),
                        correo = it.getString(5),
                        contrasena = it.getString(6),
                        genero = it.getString(7)
                    )
                )
            }
        }
        db.close()
        return list
    }

    fun getByRut(rut: String): User? {
        val db = dbHelper.readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE rut = ? LIMIT 1", arrayOf(rut))
        var u: User? = null
        c.use {
            if (it.moveToFirst()) {
                u = User(
                    rut = it.getString(0),
                    nombreUsuario = it.getString(1),
                    nombre = it.getString(2),
                    apellidoP = it.getString(3),
                    apellidoM = it.getString(4),
                    correo = it.getString(5),
                    contrasena = it.getString(6),
                    genero = it.getString(7)
                )
            }
        }
        db.close()
        return u
    }

    fun search(q: String): List<User>{
        if (q.isEmpty()) return getAll()
        val db = dbHelper.readableDatabase
        val list = mutableListOf<User>()
        val like = "%$q%"
        val c = db.rawQuery("""SELECT *  
                FROM users
                WHERE rut LIKE ? 
                OR nombreUsuario LIKE ? 
                OR nombre LIKE ? 
                OR apellidop LIKE ? 
                OR apellidom LIKE ? 
                ORDER BY apellidop""".trimIndent(),
            arrayOf(like, like, like, like, like)
        )
        c.use {
            while (it.moveToNext()) {
                list.add(
                    User(
                        rut = it.getString(0),
                        nombreUsuario = it.getString(1),
                        nombre = it.getString(2),
                        apellidoP = it.getString(3),
                        apellidoM = it.getString(4),
                        correo = it.getString(5),
                        contrasena = it.getString(6),
                        genero = it.getString(7)
                    )
                )
            }
        }
        db.close()
        return list
    }
}