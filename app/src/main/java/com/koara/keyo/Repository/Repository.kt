package com.koara.keyo.Repository

import androidx.lifecycle.LiveData
import com.koara.keyo.Model.Entity
import com.koara.keyo.database.Dao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
    Database Repository
    Nothing to do here
 */

class Repository @Inject constructor(private val dao : Dao) {

    val readAll : LiveData<List<Entity>> = dao.readAll()

    suspend fun addCredentials(entity: Entity){
        dao.addCredentials(entity)
    }

    suspend fun updateCredentials(entity: Entity){
        dao.updateCredentials(entity)
    }

    suspend fun deleteCredential(entity: Entity){
        dao.deleteCredential(entity)
    }

    fun getCredentials(searchQuery : String) : LiveData<List<Entity>>{
        return dao.getCredentials(searchQuery)
    }

}