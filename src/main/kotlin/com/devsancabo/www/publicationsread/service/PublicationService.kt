package com.devsancabo.www.publicationsread.service

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.dto.PublicationDTO

interface PublicationService {
    fun createPublicationFromEvent(publication: PublicationDTO?)
    fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO
    fun stopPopulators()
    fun gerPopulator(): GetPopulatorResponseDTO
}