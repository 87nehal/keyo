package com.koara.keyo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.koara.keyo.Model.Entity
import kotlinx.coroutines.flow.Flow

/*
    Room Database Functions & Queries
    Nothing to do here
 */

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCredentials(entity: Entity)

    @Query("SELECT * FROM keyoTable ORDER BY id DESC")
    fun readAll(): LiveData<List<Entity>>

    @Update
    suspend fun updateCredentials(entity: Entity)

    @Delete
    suspend fun deleteCredential(entity: Entity)

    @Query("SELECT * FROM keyoTable WHERE site LIKE :searchQuery OR username LIKE :searchQuery")
    fun getCredentials(searchQuery : String) : LiveData<List<Entity>>

}