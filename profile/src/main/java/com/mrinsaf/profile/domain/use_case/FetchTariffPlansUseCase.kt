package com.mrinsaf.profile.domain.use_case

import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.core.domain.model.api_result.ApiResult
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.profile.data.mapper.toTariffPlan
import javax.inject.Inject

//class FetchTariffPlansUseCase @Inject constructor(
//    private val subscriptionRepository: SubscriptionRepository
//) {
//    suspend operator fun invoke(): ApiResult<List<TariffPlanResponse>> {
//        val tariffResponse = subscriptionRepository.getAllTariffPlans()
//
//        when(tariffResponse) {
//            is ApiResult.Success -> {
//                val tariffPlansList = tariffResponse.data.map { it.toTariffPlan() }
//            }
//
//            is ApiResult.Error.NetworkError -> TODO()
//            is ApiResult.Error.ServerError -> TODO()
//            ApiResult.Error.UnknownError -> TODO()
//        }
//    }
//}