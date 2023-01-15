package com.devsancabo.www.publicationsread.populator.inserter

import com.devsancabo.www.publicationsread.dto.InserterDTO
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch

/**
 * A class that encapsulates the process of batch saving data to some destination.
 * @param <T> The type of data that's going to serve as input for the process.
</T> */
abstract class AbstractDataInserter<T> : Runnable {
    private val logger = LoggerFactory.getLogger(AbstractDataInserter::class.java)
    private var dataAmount: Int = 0
    private var latch: CountDownLatch = CountDownLatch(1)
    protected var runForever = false


    /**
     * Represents one process that takes an object or record, and saves it in an arbitrary destination.
     * @param dataAmount amount records that will be created at the destination.
     * @param latch a CountDownLatch that the inserter must call when it finishes or is interrupted
     * @param runForever Whether this inserter should execute indefinitely
     */
    protected constructor(
        dataAmount: Int,
        latch: CountDownLatch,
        runForever: Boolean
    ) {
        this.latch = latch
        this.runForever = runForever
        this.dataAmount = dataAmount
    }

    private constructor() {}

    /**
     * Actions to perform before calling the Producer
     * @return a consumer, that executes some logic before calling the supplier.
     * The supplier is meant to represent the data propducer.
     */
    abstract fun saveRecord(value : T)

    /**
     * Permits modifying the value provided by the Supplier
     * @return an instance of the data to insert.
     */
    abstract fun getRecordForSaving(): T

    /**
     * Gets an object meant to represent the inserter for use in apis.
     * @return a representation of this Inserter.
     */
    abstract val dTORepresentation: InserterDTO


    override fun run() {
        try{
            if (runForever) {
                while (true) {
                    if (!Thread.interrupted()) saveRecord(getRecordForSaving())
                    else break
                }
            } else {
                for (i in 0 until dataAmount) {
                    if (!Thread.interrupted()) saveRecord(getRecordForSaving())
                    else break
                }
            }
        } catch (e : Exception){
            logger.info("Stopping inserter because of exception: {}", e.localizedMessage)
        }
        latch.countDown()
        logger.info("Stopped inserter")
    }
}