package com.devsancabo.www.publicationsread.populator.inserter

import com.devsancabo.www.publicationsread.dto.InserterDTO
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * A class that encapsulates the process of batch saving data to some destination.
 * @param <T> The type of data that's going to serve as input for the process.
</T> */
abstract class AbstractDataInserter<T> : Runnable {
    private val logger = LoggerFactory.getLogger(AbstractDataInserter::class.java)
    private var dataAmount: Int = 0
    protected var dataProducer: Supplier<T>? = null
    private var dataPersister: Consumer<T> = Consumer { _: T -> }
    private var latch: CountDownLatch = CountDownLatch(1)
    protected var runForever = false
    private var error = false
    protected var finished = false

    /**
     * Represents one process that takes an object T from a producer, and saves it in an arbitrary destination.
     * @param dataAmount amount records that will be created at the destination.
     * @param dataProducer a source for obtaining the data to insert
     * @param dataPersister a function that handles the data saving. For example, a call to a save() on a DB.
     * @param latch a CountDownLatch that the inserter must call when it finishes or is interrupted
     * @param runForever Whether this inserter should execute indefinitely
     */
    protected constructor(
        dataAmount: Int,
        dataProducer: Supplier<T>?,
        dataPersister: Consumer<T>,
        latch: CountDownLatch,
        runForever: Boolean
    ) {
        this.dataProducer = dataProducer
        this.dataPersister = dataPersister
        this.latch = latch
        this.runForever = runForever
        this.dataAmount = if (this.runForever) 1 else dataAmount
    }

    private constructor() {
        dataAmount = 1
        dataProducer = null
        dataPersister = Consumer { _: T -> }
        latch = CountDownLatch(1)
        throw UnsupportedOperationException("Default Constructor should never be called")
    }

    private fun dataSaver(): Supplier<Boolean> {
        return Supplier {
            if (Thread.interrupted()) {
                latch.countDown()
                finished = true
            }
            val dto = handleDataForDataSaver()
            error = Objects.isNull(dto)
            if (!error) dataPersister.accept(dto)
            error
        }
    }

    /**
     * What to do before inseting one record
     * @return a consumer, that executes some logic before calling the supplier.
     * The supplier is meant to represent the data propducer.
     */
    abstract fun prepareDataForDataSaver(): Consumer<Supplier<Boolean>>

    /**
     * Permits control of how data is created.
     * @return an instance of the data to insert.
     */
    abstract fun handleDataForDataSaver(): T

    /**
     * Gets an object meant to represent the inserter for use in apis.
     * @return a representation of this Inserter.
     */
    abstract val dTORepresentation: InserterDTO
    override fun run() {
        if (runForever) {
            while (!error && !finished) {
                prepareDataForDataSaver().accept(dataSaver())
            }
        } else {
            for (i in 0 until dataAmount) {
                if (error || finished) break
                prepareDataForDataSaver().accept(dataSaver())
            }
        }
        latch.countDown()
        finished = true
        logger.info("Stopped inserter: error={}", error)
    }
}