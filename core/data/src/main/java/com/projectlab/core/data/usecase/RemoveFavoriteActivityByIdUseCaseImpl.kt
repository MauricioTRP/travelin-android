package com.projectlab.core.data.usecase

import com.projectlab.core.domain.repository.ActivityRepository
import com.projectlab.core.domain.repository.UserSessionProvider
import com.projectlab.core.domain.use_cases.activities.RemoveFavoriteActivityByIdUseCase
import javax.inject.Inject

class RemoveFavoriteActivityByIdUseCaseImpl @Inject constructor(
    private val activitiesRepository: ActivityRepository,
) : RemoveFavoriteActivityByIdUseCase {
    override suspend fun invoke(activityId: String): Result<Unit> =
        activitiesRepository.removeFavoriteActivityById(activityId)
}
