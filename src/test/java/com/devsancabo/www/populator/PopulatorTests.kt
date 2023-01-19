package com.devsancabo.www.populator

import com.devsancabo.www.populator.populator.*
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
}