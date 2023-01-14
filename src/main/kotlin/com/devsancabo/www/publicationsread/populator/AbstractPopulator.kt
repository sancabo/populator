package com.devsancabo.www.publicationsread.populator

import com.devsancabo.www.publicationsread.dto.GetPopulatorResponseDTO
import com.devsancabo.www.publicationsread.populator.inserter.AbstractDataInserter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Supplier

abstract class AbstractPopulator<T> protected constructor(
    private var dataProducer: Supplier<T>, protected var dataPersister: Consumer<T>,
    private val amountToInsert: Int, private val timeoutInMillis: Int
) : Populator<T> {
    private val populatorMap: MutableMap<String, Thread> = HashMap()
    private var currentIntensity: Int = 1
    private var latch: CountDownLatch? = null
    private var status: Status = Status.UNINITIALIZED
    private var currentInserter: AbstractDataInserter<T>? = null
    private var runForever = true

    enum class Status {
        UNINITIALIZED, RUNNING, FAULTED, STOPPING, STOPPED, STOPPED_WITH_ERRORS
    }

    //TODO: I want the populator to stop itself when it finishes
    //TODO: I want to add cadence to inserters
    //TODO: I want to be able to modify the populator while it's running
    //TODO: Use an executor service instead of creating threads manually

    abstract fun getInserter(
        amountToInsert: Int,
        dataProducer: Supplier<T>,
        dataPersister: Consumer<T>,
        latch: CountDownLatch,
        runForever: Boolean
    ): AbstractDataInserter<T>

    override fun startPopulator(intensity: Int, runForever: Boolean): GetPopulatorResponseDTO {
        if (status == Status.FAULTED) {
            throw UnsupportedOperationException("Populator is faulty. Please Restart It.")
        }
        if (status != Status.RUNNING) {
            status = Status.RUNNING
            currentIntensity = intensity
            latch = CountDownLatch(currentIntensity)
            populatorMap.clear()
            this.runForever = runForever
            for (i in 1 until currentIntensity + 1) {
                val dbInserter = getInserter(amountToInsert, dataProducer, dataPersister, latch!!,
                    this.runForever
                )
                currentInserter = dbInserter
                val thread = Thread(dbInserter)
                thread.name = "DbInserter-$i"
                thread.start()
                populatorMap[thread.name] = thread
            }
        }
        Thread({ waitForInsertersToEnd() }, "populator-stopper").start()
        return populatorDTO
    }

    override fun stopPopulator() {
        if (status == Status.RUNNING) {
            status = Status.STOPPING
            populatorMap.forEach { (_, v: Thread) -> v.interrupt() }
        }
    }

    override val populatorDTO: GetPopulatorResponseDTO
        get() = GetPopulatorResponseDTO.builder()
            .activeInserterCount(
                populatorMap.entries.stream().filter {
                        (_, value): Map.Entry<String, Thread> -> !value.isInterrupted && value.isAlive}
                    .count())
            .inserterCount(populatorMap.size)
            .runForever(runForever)
            .insetionsPerThread(amountToInsert)
            .status(status)
            .intensity(currentIntensity)
            .inserterSpec(currentInserter?.dTORepresentation)
            .build()

    override fun resetPopulator(): GetPopulatorResponseDTO {
        latch = null
        status = Status.UNINITIALIZED
        currentIntensity = 1
        populatorMap.clear()
        return populatorDTO
    }


    private fun waitForInsertersToEnd(){

        var countedToZero = false
        try {
            countedToZero = latch!!.await(timeoutInMillis.toLong(), TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            latch = null
            status = Status.FAULTED
            Thread.currentThread().interrupt()
        }
        populatorMap.clear()
        status = if (countedToZero) {
            Status.STOPPED
        } else {
            Status.STOPPED_WITH_ERRORS
        }
        TODO("Fix the timeout")
    }
}