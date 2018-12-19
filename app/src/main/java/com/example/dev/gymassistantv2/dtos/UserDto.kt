package com.example.dev.gymassistantv2.dtos

import com.example.dev.gymassistantv2.entities.User
import java.io.Serializable

class UserDto (var userId: Long?, var facebookId: Long?, var isTrainer: Boolean?, var trainerId: Long?, var firstName: String?, var lastName: String?): Serializable
{
    constructor(user: User):this(userId = user.id, facebookId = user.facebookId, isTrainer = user.isTrainer,
            trainerId = user.trainerId, firstName = user.firstName, lastName = user.lastName)

    public fun toUser(): User {
        var user = User()
        user.id = this.userId
        user.facebookId = this.facebookId
        user.isTrainer = this.isTrainer
        user.trainerId = this.trainerId
        user.firstName = this.firstName
        user.lastName = this.lastName
        return user
    }
}