package com.devsancabo.www.populator.populator

import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class TestInserterFactory : InserterFactory<TestDataInserter> {

    override fun createInserter(amountToInsert: Int, latch: CountDownLatch, runForever: Boolean): TestDataInserter =
        TestDataInserter(amountToInsert,latch,runForever)
}