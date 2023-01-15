package com.devsancabo.www.publicationsread.controller

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.service.PopulatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PopulatorController @Autowired constructor(private val populatorService: PopulatorService) {
    @PostMapping("/populator")
    fun populate(@RequestParam(name = "intensity") intensity: Int,
                 @RequestParam(name = "runForever") runForever: Boolean): ResponseEntity<GetPopulatorResponseDTO> =
        ResponseEntity.ok(populatorService.startPopulator(intensity, runForever))

    @PostMapping("/populator/reset")
    fun reset(): ResponseEntity<GetPopulatorResponseDTO> =
        ResponseEntity.ok(populatorService.resetPopulator())


    @DeleteMapping("/populator")
    fun stopPopulate(): ResponseEntity<Any> {
        populatorService.stopPopulators()
        return ResponseEntity.ok("Requested stopping of data Population")
    }

    @GetMapping("/populator")
    fun populator(): ResponseEntity<GetPopulatorResponseDTO> =
         ResponseEntity.ok(populatorService.gerPopulator())
}