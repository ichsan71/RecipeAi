package com.cl.swafoody.data.source.local.entity

import android.net.Uri
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_entities")
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String? = null,
)