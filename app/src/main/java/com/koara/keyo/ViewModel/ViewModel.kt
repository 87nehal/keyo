package com.koara.keyo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.koara.keyo.Model.Entity
import com.koara.keyo.Repository.Repository
import com.koara.keyo.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
    Nothing to do here
 */
open class ViewModel(application: Application) : AndroidViewModel(application) {

    val readAll : LiveData<List<Entity>>
    private val repository : Repository
    init {
        val dao = Databse.getDatabase(application).dao()
        repository = Repository(dao)
        readAll = repository.readAll
    }

    fun addCredentials(entity: Entity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCredentials(entity)
        }
    }

    fun updateCredentials(entity: Entity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCredentials(entity)
        }
    }

    fun deleteCredential(entity: Entity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCredential(entity)
        }
    }

    fun getCredentials(searchQuery : String): LiveData<List<Entity>> {
        return repository.getCredentials(searchQuery)
    }

}