package com.devsancabo.www.publicationsread.controller

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.service.PublicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PopulatorController @Autowired constructor(private val publicationService: PublicationService) {
    @PostMapping("/populator")
    fun populate(@RequestParam(name = "intensity") intensity: Int,
                 @RequestParam(name = "runForever") runForever: Boolean): ResponseEntity<GetPopulatorResponseDTO> {
        return ResponseEntity.ok(publicationService.startPopulator(intensity, runForever))
    }

    @DeleteMapping("/populator")
    fun stopPopulate(): ResponseEntity<Any> {
        publicationService.stopPopulators()
        return ResponseEntity.ok("Requested stopping of data Population")
    }

    @get:GetMapping("/populator")
    val populator: ResponseEntity<GetPopulatorResponseDTO>
        get() = ResponseEntity.ok(publicationService.gerPopulator())
}