package com.example.dev.gymassistantv2.DTOs

import com.example.dev.gymassistantv2.Entities.Invitation
import java.io.Serializable

class PendingInvitationDto (var id: Long?, var senderId: Long?, var recipientId: Long?, var date: Long?): Serializable
{
    constructor(invitation: Invitation):this(id = invitation.id, senderId = invitation.senderId, recipientId = invitation.recipientId, date = invitation.date)
    constructor():this(id = null, senderId = null, recipientId = null, date = null)
}