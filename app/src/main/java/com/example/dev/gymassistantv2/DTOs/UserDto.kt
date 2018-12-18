package com.example.dev.gymassistantv2.DTOs

import com.example.dev.gymassistantv2.Entities.User
import java.io.Serializable

class UserDto (var userId: Long?, var facebookId: Long?, var isTrainer: Boolean?, var trainerId: Long?, var firstName: String?, var lastName: String?): Serializable
{
    constructor(user: User, firstName: String?, lastName: String? ):this(userId = user.id, facebookId = user.facebookId, isTrainer = user.isTrainer,
            trainerId = user.trainerId, firstName = firstName, lastName = lastName)
}