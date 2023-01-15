package com.devsancabo.www.publicationsread.dto

import com.devsancabo.www.publicationsread.populator.DefaultPopulator


data class GetPopulatorResponseDTO(val status: DefaultPopulator.Status? = null,
                                   val timeInCurrentStatus: Long? = null,
                                   val insertionsPerThread: Int? = null,
                                   val runForever: Boolean? = null,
                                   val intensity: Int? = null,
                                   val inserterCount: Int? = null,
                                   val activeInserterCount: Long? = null,
                                   val inserterSpec: InserterDTO? = null) {

    class Builder(
        private var statusB: DefaultPopulator.Status? = null,
        private var timeSinceLastStateChangeB: Long? = null,
        private var insertionsPerThreadB: Int? = null,
        private var runForeverB: Boolean? = null,
        private var intensityB: Int? = null,
        private var inserterCountB: Int? = null,
        private var activeInserterCountB: Long? = null,
        private var inserterSpecB : InserterDTO? = null) {
        fun timeInCurrentStatus(timeInCurrentStatus: Long) = apply {
            this.timeSinceLastStateChangeB = timeInCurrentStatus}
        fun activeInserterCount(activeInserterCount: Long) = apply {this.activeInserterCountB = activeInserterCount}
        fun inserterCount(inserterCount: Int) = apply {this.inserterCountB = inserterCount}
        fun runForever(runForever: Boolean) = apply {this.runForeverB = runForever}
        fun insertionsPerThread(insertionsPerThread: Int) = apply {this.insertionsPerThreadB = insertionsPerThread}
        fun status(status: DefaultPopulator.Status?) = apply {this.statusB = status}
        fun intensity(intensity: Int) = apply {this.intensityB = intensity}
        fun inserterSpec(inserterSpec: InserterDTO?) = apply {this.inserterSpecB = inserterSpec}
        fun build() = GetPopulatorResponseDTO(statusB, timeSinceLastStateChangeB,
            insertionsPerThreadB, runForeverB, intensityB, inserterCountB, activeInserterCountB, inserterSpecB)
    }

    companion object {
        fun builder(): Builder {return Builder()}
    }
}