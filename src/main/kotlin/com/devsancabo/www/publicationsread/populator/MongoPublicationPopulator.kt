package com.devsancabo.www.publicationsread.populator

import com.devsancabo.www.publicationsread.dto.PublicationDTO
import com.devsancabo.www.publicationsread.populator.inserter.AbstractDataInserter
import com.devsancabo.www.publicationsread.populator.inserter.MongoInserter
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer
import java.util.function.Supplier

class MongoPublicationPopulator  //TODO: Complete
    (dataProducer: Supplier<PublicationDTO>, dataConsoomer: Consumer<PublicationDTO>) :
    AbstractPopulator<PublicationDTO>(dataProducer, dataConsoomer, AMOUNT_PER_INSERTER, TIMEOUT_IN_MILLIS) {
    override fun getInserter(
        amountToInsert: Int?,
        dataProducer: Supplier<PublicationDTO>?,
        dataPersister: Consumer<PublicationDTO>,
        latch: CountDownLatch,
        runForever: Boolean
    ): AbstractDataInserter<PublicationDTO> {
        return MongoInserter(AMOUNT_PER_INSERTER, dataProducer, dataPersister, latch, runForever)
    }

    companion object {
        private const val AMOUNT_PER_INSERTER = 1000
        private const val TIMEOUT_IN_MILLIS = 1000
    }
}