package com.devsancabo.www.populator.populator

import com.devsancabo.www.populator.populator.inserter.AbstractDataInserter
import java.util.concurrent.CountDownLatch

/**
 * This class is used in the populator to create data inserters of type T.
 */
interface InserterFactory<out T : AbstractDataInserter<*>> {
    fun createInserter(amountToInsert : Int, latch : CountDownLatch, runForever : Boolean) : T
}