package com.devsancabo.www.publicationsread.populator

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO

interface Populator<T> {
    fun startPopulator(intensity: Int?, runForever: Boolean): GetPopulatorResponseDTO
    fun stopPopulator()
    val populatorDTO: GetPopulatorResponseDTO
    fun resetPopulator(): GetPopulatorResponseDTO
}