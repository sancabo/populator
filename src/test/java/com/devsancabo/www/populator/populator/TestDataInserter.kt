package com.devsancabo.www.populator.populator

import com.devsancabo.www.populator.dto.InserterDTO
import com.devsancabo.www.populator.populator.inserter.AbstractDataInserter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CountDownLatch

class TestDataInserter(dataAmount: Int, latch: CountDownLatch, runForever: Boolean) :
    AbstractDataInserter<String>(dataAmount, latch, runForever) {

    private val logger : Logger = LoggerFactory.getLogger(javaClass)
    override fun saveRecord(value: String) {
        logger.info("Saved record : $value")
    }

    override fun getRecordForSaving(): String {
        return UUID.randomUUID().toString()
    }

    override val dTORepresentation: InserterDTO = InserterDTO("TestInserter", HashMap(),
        "A test data inserter. Ingests strings and writes them to log")
}