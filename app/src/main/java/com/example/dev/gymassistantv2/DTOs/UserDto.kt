package com.example.dev.gymassistantv2.DTOs

import com.example.dev.gymassistantv2.Entities.User
import java.io.Serializable

class UserDto (var userId: Long?, var facebookId: Long?, var isTrainer: Boolean?, var trainerId: Long?): Serializable
{
    constructor(user: User):this(userId = user.id, facebookId = user.facebookId, isTrainer = user.isTrainer, trainerId = user.trainerId)
}