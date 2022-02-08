package com.koara.keyo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koara.keyo.Model.Entity

@Database(entities = [Entity::class], version = 1, exportSchema = false)
abstract class Databse : RoomDatabase() {

    abstract fun dao() : Dao

    companion object{
        @Volatile
        private var INSTANCE : Databse ?= null

        fun getDatabase(context : Context) : Databse{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Databse::class.java,
                    "keyoTable"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}