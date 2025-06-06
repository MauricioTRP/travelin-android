package com.projectlab.core.database.services

import com.projectlab.core.domain.entity.ActivityEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow

interface FirestoreActivity {

    suspend fun createActivity(activity: ActivityEntity): Result<EntityId>
    suspend fun getActivityById(
        userId: String, itinId: String, activityId: String
    ): Result<ActivityEntity?>
    suspend fun getAllActivitiesForItinerary(
        userId: String, itinId: String
    ): Result<List<ActivityEntity>>
    suspend fun updateActivity(activity: ActivityEntity): Result<Unit>
    suspend fun deleteActivity(
        userId: String, itinId: String, activityId: String
    ): Result<Unit>
}