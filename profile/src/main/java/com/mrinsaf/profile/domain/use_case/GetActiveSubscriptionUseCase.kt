package com.mrinsaf.profile.domain.use_case

import android.os.Build
import androidx.annotation.RequiresApi
import com.mrinsaf.core.domain.model.api_result.ApiResult
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.profile.domain.model.ActiveSubscription
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration

class GetActiveSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(): ApiResult<ActiveSubscription> {
        val subscriptionResult = subscriptionRepository.getActiveSubscription()
        if (subscriptionResult is ApiResult.Error) return subscriptionResult
        val subscription = (subscriptionResult as ApiResult.Success).data

        val tariffResult = subscriptionRepository.getTariffPlanById(subscription.tariffPlanId)
        if (tariffResult is ApiResult.Error) return tariffResult
        val tariff = (tariffResult as ApiResult.Success).data

        val daysLeft = calculateDaysUntilExpiration(subscription.endDate)

        return ApiResult.Success(
            ActiveSubscription(
                tariffName = tariff.name,
                daysUntilExpiration = daysLeft
            )
        )
    }
    private fun calculateDaysUntilExpiration(endDateString: String): Int {
        val parsedDate = Instant.parse(endDateString)
        val now = Clock.System.now()

        val difference: Duration = now - parsedDate

        val daysDifference = difference.inWholeDays
        println("Разница в днях: $daysDifference")
        return daysDifference.toInt()
    }
}