package com.devsancabo.www.publicationsread.populator

import com.devsancabo.www.publicationsread.populator.inserter.AbstractDataInserter
import java.util.concurrent.CountDownLatch

interface InserterFactory<out T : AbstractDataInserter<*>> {
    fun createInserter(amountToInsert : Int, latch : CountDownLatch, runForever : Boolean) : T
}