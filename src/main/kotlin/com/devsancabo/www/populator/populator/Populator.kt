package com.devsancabo.www.populator.populator

import com.devsancabo.www.populator.dto.GetPopulatorResponseDTO

sealed interface Populator<T> {
    fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO
    fun stopPopulator()

    val populatorDTO: GetPopulatorResponseDTO
    fun resetPopulator(): GetPopulatorResponseDTO
}