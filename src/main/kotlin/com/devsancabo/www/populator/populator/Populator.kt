package com.devsancabo.www.populator.populator

import com.devsancabo.www.populator.dto.GetPopulatorResponseDTO

/**
 * Provides methods to operate a Populator of a datatype T
 */
sealed interface Populator<T> {

    /**
     * Starts the populator instance.
     * Has no effect if the populator is already running.
     * @param intensity - the number of inserter thread that will be started
     * @param runForever - inserter threads will ignore the amount parameter and insert data until manually stopped.
     * @return A GetPopulatorResponseDTO instance representating the populator state.
     */
    fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO

    /**
     * Stops the currently running populator. This will make all its threads stop.
     * Has no effect if the populator isn't running.
     */
    fun stopPopulator()

    /**
     * Get populator data.
     * @return A GetPopulatorResponseDTO instance representating the populator state.
     */
    val populatorDTO: GetPopulatorResponseDTO

    /**
     * Reverts the populator back to it's initial state. Used to recover from FAILED states.
     */
    fun resetPopulator(): GetPopulatorResponseDTO
}