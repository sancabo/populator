package com.devsancabo.www.publicationsread.service

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.populator.Populator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PopulatorServiceImpl @Autowired constructor(private val populator: Populator<Any>) : PopulatorService {
    private val logger = LoggerFactory.getLogger(PopulatorServiceImpl::class.java)

    override fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO {
        return populator.startPopulator(intensity, runForever)
    }

    override fun stopPopulators() {
        populator.stopPopulator()
    }

    override fun gerPopulator(): GetPopulatorResponseDTO {
        return populator.populatorDTO
    }
}