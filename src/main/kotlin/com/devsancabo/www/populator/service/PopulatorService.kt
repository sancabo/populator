package com.devsancabo.www.populator.service

import com.devsancabo.www.populator.dto.GetPopulatorResponseDTO

interface PopulatorService {
    fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO
    fun stopPopulators()
    fun gerPopulator(): GetPopulatorResponseDTO
    fun resetPopulator(): GetPopulatorResponseDTO
}