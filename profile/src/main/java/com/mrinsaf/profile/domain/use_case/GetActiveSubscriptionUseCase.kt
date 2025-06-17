package com.mrinsaf.profile.domain.use_case

import android.os.Build
import androidx.annotation.RequiresApi
import com.mrinsaf.core.domain.model.api_result.ApiResult
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.profile.domain.model.ActiveSubscription
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetActiveSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): ApiResult<ActiveSubscription> {
        val subscriptionResult = subscriptionRepository.getActiveSubscription()
        when (subscriptionResult ) {
            is ApiResult.Error -> return subscriptionResult
            is ApiResult.Success -> {
                val subscription = subscriptionResult.data
                val tariffResult = subscriptionRepository.getTariffPlanById(subscription.tariffPlanId)
                when (tariffResult) {
                    is ApiResult.Error -> return tariffResult
                    is ApiResult.Success -> {
                        val tariff = tariffResult.data
                        val daysLeft = calculateDaysUntilExpiration(subscription.endDate)
                        return ApiResult.Success(
                            ActiveSubscription(
                                tariffName = tariff.name,
                                daysUntilExpiration = daysLeft
                            )
                        )
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDaysUntilExpiration(endDateString: String): Int {
        // Парсим дату с учётом времени и часового пояса
        val endDate = ZonedDateTime.parse(endDateString, DateTimeFormatter.ISO_INSTANT)

        // Получаем текущее время в том же часовом поясе (UTC)
        val now = ZonedDateTime.now(ZoneOffset.UTC)

        // Вычисляем разницу в днях
        return ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt()
    }
}