package com.koara.keyo.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
    Format Room Database
    Nothing to do here
 */

@Parcelize
@Entity(tableName = "keyoTable")
data class Entity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val site : String,
    val username : String,
    val password : String
) : Parcelable