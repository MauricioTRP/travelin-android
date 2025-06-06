package com.projectlab.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.projectlab.core.data.mapper.toDomain
import com.projectlab.core.data.remote.ActivityApiService
import com.projectlab.core.database.services.FirestoreActivity
import com.projectlab.core.domain.entity.ActivityEntity
import com.projectlab.core.domain.entity.FavoriteActivityEntity
import com.projectlab.core.domain.model.Activity
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.repository.ActivityRepository
import com.projectlab.core.domain.util.DataError
import com.projectlab.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * ActivityRepositoryImpl is a concrete implementation of the ActivityRepository interface.
 * It provides methods to interact with activities in Data Bases and external APIs.
 *
 * @param firestore The FirebaseFirestore instance used to interact with Firestore.
 * @param activityApiService The ActivityApiService instance used to fetch
 * activities from an external API.
 */

class ActivityRepositoryImpl @Inject constructor(
    private val firestoreActivity: FirestoreActivity,
    private val firestore: FirebaseFirestore,
    private val activityApiService: ActivityApiService,
) : ActivityRepository {
    override suspend fun createActivity(activity: ActivityEntity): kotlin.Result<EntityId> {
        return firestoreActivity.createActivity(activity)
    }

    override suspend fun getActivityById(
        userId: String,
        itinId: String,
        activityId: String
    ): kotlin.Result<ActivityEntity?> {
        return firestoreActivity.getActivityById(userId, itinId, activityId)
    }

    override suspend fun getAllActivities(
        userId: String,
        itinId: String
    ): kotlin.Result<List<ActivityEntity>> {
        return firestoreActivity.getAllActivitiesForItinerary(userId, itinId)
    }

    override suspend fun updateActivity(activity: ActivityEntity): kotlin.Result<Unit> {
        return firestoreActivity.updateActivity(activity)
    }

    override suspend fun deleteActivity(
        userId: String,
        itinId: String,
        activityId: String
    ): kotlin.Result<Unit> {
        return firestoreActivity.deleteActivity(userId, itinId, activityId)
    }

    override suspend fun getAPIActivityById(
        id: String,
    ): Result<Activity, DataError.Network> {
        return try {
            val response = activityApiService.getActivityById(id)
            Result.Success(response.data.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }



    override fun queryFavoriteActivities(
        userId: String,
        nameQuery: String?,
    ): Flow<FavoriteActivityEntity> = flow {
        val userDoc = firestore
            .collection("Users")
            .document(userId)

        val documents = userDoc
            .collection("FavoriteActivities")
            .get()
            .await()

        documents.map { doc ->
            val activity = doc.toObject(FavoriteActivityEntity::class.java)

            val include = nameQuery == null
                || nameQuery.isEmpty()
                || activity.name.contains(nameQuery, ignoreCase = true)

            if (include) {
                emit(activity)
            }
        }
    }

    override suspend fun isFavoriteActivity(
        userId: String,
        activityId: String,
    ): kotlin.Result<Boolean> = runCatching {
        val userDoc = firestore
            .collection("Users")
            .document(userId)

        val document = userDoc
            .collection("FavoriteActivities")
            .document(activityId)
            .get()
            .await()

        document != null
    }

    override suspend fun saveFavoriteActivity(
        userId: String,
        activity: FavoriteActivityEntity,
    ): kotlin.Result<Unit> = runCatching {
        val userDoc = firestore
            .collection("Users")
            .document(userId)

        val favoritesCollection = userDoc.collection("FavoriteActivities")
        val docRef = favoritesCollection.document(activity.id)
        docRef.set(activity).await()
    }

    override suspend fun removeFavoriteActivityById(
        userId: String,
        activityId: String,
    ): kotlin.Result<Unit> = runCatching {
        val userDoc = firestore
            .collection("Users")
            .document(userId)

        val activityDoc = userDoc
            .collection("FavoriteActivities")
            .document(activityId)

        activityDoc.delete().await()
    }

    override suspend fun getActivitiesByCoordinates(
        latitude: Double,
        longitude: Double,
    ): Result<List<Activity>, DataError.Network> {
        return try {
            val response = activityApiService.getActivitiesByLocation(latitude, longitude)
            response.data
            Result.Success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
