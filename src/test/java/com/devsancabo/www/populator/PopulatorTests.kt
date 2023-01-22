package com.devsancabo.www.populator

import com.devsancabo.www.populator.populator.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant


class PopulatorTests {

    private val inserterFactory : InserterFactory<TestDataInserter> = TestInserterFactory()
    private val populator : Populator<String> = DefaultPopulator(inserterFactory)

    @Test
    fun testLowIntensity() {
        populator.startPopulator(2, false)
        val start = Instant.now()
        while(!DefaultPopulator.Status.STOPPED.equals(populator.populatorDTO.status)){
            if (Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli() > 5000L) AssertionError("Did not stop")
        }
    }

    @Test
    fun testMediumLevelIntensity() {
        populator.startPopulator(10, false)
        val start = Instant.now()
        while(!DefaultPopulator.Status.STOPPED.equals(populator.populatorDTO.status)){
            if (Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli() > 5000L) AssertionError("Did not stop")
        }
    }

    @Test
    fun testStartStop() {
        populator.startPopulator(2, true)
        Assertions.assertEquals(DefaultPopulator.Status.RUNNING,populator.populatorDTO.status)
        Thread.sleep(500)
        populator.stopPopulator()
        Assertions.assertEquals(DefaultPopulator.Status.STOPPED,populator.populatorDTO.status)
    }

    @Test
    fun illegalStateTransitions() {
        populator.startPopulator(2, true)
        Assertions.assertThrows(IllegalStateException::class.java) { populator.startPopulator(2, true) }
        Assertions.assertEquals(DefaultPopulator.Status.RUNNING,populator.populatorDTO.status)
        Assertions.assertEquals(2,populator.populatorDTO.activeInserterCount)
        populator.stopPopulator()
    }
}