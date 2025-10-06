package com.example.accesapp.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(
    context,
    "app_pv_sqlite.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""  
                 CREATE TABLE users(
                     rut TEXT PRIMARY KEY,
                     nombreUsuario TEXT NOT NULL,
                     nombre TEXT NOT NULL,
                     apellidop TEXT NOT NULL,
                     apellidom TEXT NOT NULL,
                     correo TEXT NOT NULL,
                     contrasena TEXT NOT NULL,
                     genero TEXT NOT NULL
                 )
                 """.trimIndent()
        )
    }

    override fun onUpgrade( db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}