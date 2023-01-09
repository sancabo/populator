package com.devsancabo.www.publicationsread.dto

import com.devsancabo.www.publicationsread.populator.AbstractPopulator


data class GetPopulatorResponseDTO(val status: AbstractPopulator.Status? = null,
                                   val insetionsPerThread: Int? = null,
                                   val runForever: Boolean? = null,
                                   val intensity: Int? = null,
                                   val inserterCount: Int? = null,
                                   val activeInserterCount: Long? = null,
                                   val inserterSpec: InserterDTO? = null) {

    data class Builder(var status: AbstractPopulator.Status? = null,
                       var insetionsPerThread: Int? = null,
                       var runForever: Boolean? = null,
                       var intensity: Int? = null,
                       var inserterCount: Int? = null,
                       var activeInserterCount: Long? = null,
                       var inserterSpec: InserterDTO? = null) {

        fun activeInserterCount(activeInserterCount: Long?) = apply {this.activeInserterCount = activeInserterCount}
        fun inserterCount(inserterCount: Int?) = apply {this.inserterCount = inserterCount}
        fun runForever(runForever: Boolean?) = apply {this.runForever = runForever}
        fun insetionsPerThread(insetionsPerThread: Int?) = apply {this.insetionsPerThread = insetionsPerThread}
        fun status(status: AbstractPopulator.Status?) = apply {this.status = status}
        fun intensity(intensity: Int) = apply {this.intensity = intensity}
        fun inserterSpec(inserterSpec: InserterDTO?) = apply {this.inserterSpec = inserterSpec}
        fun build() = GetPopulatorResponseDTO(status,
            insetionsPerThread, runForever, intensity, inserterCount, activeInserterCount, inserterSpec)
    }

    companion object {
        fun builder(): Builder {return Builder()}
    }
}