package com.example.dev.gymassistantv2.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "User")
data class User(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name="facebookId") var facebookId: Long?,
                @ColumnInfo(name="isTrainer") var isTrainer: Boolean?,
                @ColumnInfo(name="trainerId") var trainerId: Long?,
                @ColumnInfo(name="firstName") var firstName: String?,
                @ColumnInfo(name="lastName") var lastName: String?
){
    constructor():this(null, null, false, null, null, null)
    constructor(facebookId: Long?, firstName: String?, lastName: String?):this(null, facebookId, false, null, firstName, lastName)
    constructor(facebookId: Long?, isTrainer: Boolean?, firstName: String?, lastName: String?):this(null, facebookId, isTrainer, null, firstName, lastName)
    constructor(facebookId: Long?, isTrainer: Boolean?, trainerId: Long, firstName: String?, lastName: String?):this(null, facebookId, isTrainer, trainerId, firstName, lastName)

}