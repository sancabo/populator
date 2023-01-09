package com.devsancabo.www.publicationsread.service

import com.devsancabo.www.publicationsread.dto.AuthorDTO
import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.dto.PublicationDTO
import com.devsancabo.www.publicationsread.populator.MongoPublicationPopulator
import com.devsancabo.www.publicationsread.populator.Populator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PublicationServiceImpl internal constructor() : PublicationService {
    private val logger = LoggerFactory.getLogger(PublicationServiceImpl::class.java)
    private val populator: Populator<PublicationDTO>

    init {
        populator = MongoPublicationPopulator(
            {
                PublicationDTO.builder()
                    .author(AuthorDTO(UUID.randomUUID().leastSignificantBits, UUID.randomUUID().toString()))
                    .content("")
                    .datetime(LocalDateTime.now())
                    .build()
            },
            { publicationDTO: PublicationDTO? -> createPublicationFromEvent(publicationDTO) }
        )
    }

    override fun createPublicationFromEvent(publication: PublicationDTO?) {
        logger.info("Process event - create publication - [{}]", publication)
        logger.info("Publication Created")
    }

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