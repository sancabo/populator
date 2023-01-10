package com.devsancabo.www.publicationsread.service

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO

interface PopulatorService {
    fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO
    fun stopPopulators()
    fun gerPopulator(): GetPopulatorResponseDTO
}