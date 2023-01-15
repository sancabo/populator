package com.devsancabo.www.publicationsread.populator

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.populator.inserter.AbstractDataInserter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.CountDownLatch

abstract class AbstractPopulator<T> protected constructor(
    private val amountToInsert: Int, private val timeoutInMillis: Int,
) : Populator<T> {
    private val logger : Logger = LoggerFactory.getLogger(AbstractPopulator::class.java)
    private val populatorMap: MutableMap<String, Thread> = HashMap()
    private var currentIntensity: Int = 1
    private var latch: CountDownLatch = CountDownLatch(1)
    private var currentStatus: Status = Status.UNINITIALIZED
    private var currentInserter: AbstractDataInserter<T>? = null
    private var runForever = true
    private var lastStateChangeTime : Long = Instant.now().toEpochMilli()
    private var waitForEndThread :Thread? = null
    private val statusLock : Any = Any()

    enum class Status(val allowed : Array<Short>) {
        UNINITIALIZED(arrayOf(1)), //0
        RUNNING(arrayOf(3,4,5)), //1
        FAULTED(arrayOf(0)), //2
        STOPPING(arrayOf(2,4,5)), //3
        STOPPED(arrayOf(1,0)), //4
        STOPPED_WITH_ERRORS(arrayOf(1,0)) //5


    }
    private fun goTo(newStatus : Status, handler: Runnable = Runnable{}){
        synchronized(statusLock) {
            if (!currentStatus.allowed.contains(newStatus.ordinal.toShort())) {
                throw IllegalStateException("Cannot perform operation: Illegal state transition form $this to $newStatus")
            }
            currentStatus = newStatus
            lastStateChangeTime = Instant.now().toEpochMilli()
            handler.run()
        }
    }


    /*
    TODO: Make into a component. Set the inserter by composition
    TODO: I want to add cadence to inserters
    TODO: I want to be able to modify the populator while it's running
    TODO: Use an executor service instead of creating threads manually
    */

    abstract fun getInserter(
        amountToInsert: Int,
        latch: CountDownLatch,
        runForever: Boolean
    ): AbstractDataInserter<T>

    override fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO {
        logger.info("Starting Populator.")
        goTo(Status.RUNNING)
        lastStateChangeTime = Instant.now().toEpochMilli()
        currentIntensity = intensity
        latch = CountDownLatch(currentIntensity)
        populatorMap.clear()
        this.runForever = runForever
        for (i in 1 until currentIntensity + 1) {
            val dbInserter = getInserter(amountToInsert, latch,this.runForever)
            currentInserter = dbInserter
            val thread = Thread(dbInserter)
            thread.name = "DbInserter-$i"
            thread.start()
            populatorMap[thread.name] = thread
        }
        logger.info("Populator Started.")
        waitForEndThread = Thread({ waitForInsertersToEnd() }, "populator-stopper")
        waitForEndThread?.start()
        return populatorDTO
    }

    override fun stopPopulator() {
        logger.info("Stopping Populator.")
        goTo(Status.STOPPING)
        populatorMap.forEach { (_, v: Thread) -> v.interrupt() }
        waitForEndThread?.join(5000)
        if (checkNotNull(waitForEndThread?.isAlive)) {
            logger.info("Stopping timeout exceeded. Forcefully stopping instead.")
            waitForEndThread?.interrupt()
            waitForEndThread?.join(1000)
            if (checkNotNull(waitForEndThread?.isAlive)) {
                logger.info("Couldn't stop Populator. It needs to be restarted")
                goTo(Status.FAULTED)
            }
        }

        logger.info("Populator stopped.")
    }

    override val populatorDTO: GetPopulatorResponseDTO
        get() {
            val resultDTO =
                GetPopulatorResponseDTO.builder()
                .activeInserterCount(
                    populatorMap.entries.stream().filter {
                            (_, value): Map.Entry<String, Thread> -> !value.isInterrupted && value.isAlive}
                        .count())
                .inserterCount(populatorMap.size)
                .runForever(runForever)
                .insertionsPerThread(amountToInsert)
                .status(currentStatus)
                .intensity(currentIntensity)
                .inserterSpec(currentInserter?.dTORepresentation)
                .timeInCurrentStatus(Instant.now().toEpochMilli() - lastStateChangeTime)
                .build()
            return resultDTO}

    override fun resetPopulator(): GetPopulatorResponseDTO {
        logger.info("Resetting Populator.")
        goTo(Status.UNINITIALIZED)
        latch = CountDownLatch(1)
        currentIntensity = 1
        populatorMap.clear()
        waitForEndThread = null
        return populatorDTO
    }

    private fun waitForInsertersToEnd(){
        var newStatus = Status.STOPPED
        try {
            latch.await()
        } catch (e: InterruptedException) {
            logger.info("Requested premature stop")
            Thread.currentThread().interrupt()
            newStatus = Status.STOPPED_WITH_ERRORS
        }
        logger.info("All inserters stopped. Populator finished.")
        populatorMap.clear()
        goTo(newStatus)

    }
}