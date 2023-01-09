package com.devsancabo.www.publicationsread.populator.inserter

import com.devsancabo.www.publicationsread.dto.InserterDTO
import com.devsancabo.www.publicationsread.dto.PublicationDTO
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer
import java.util.function.Supplier

class MongoInserter
    (
    amountToInsert: Int,
    dataProducer: Supplier<PublicationDTO>?,
    dataPersister: Consumer<PublicationDTO>,
    latch: CountDownLatch,
    runForever: Boolean
) : AbstractDataInserter<PublicationDTO>(amountToInsert, dataProducer, dataPersister, latch, runForever) {
    private var publication: PublicationDTO = super.dataProducer!!.get()
    override fun prepareDataForDataSaver(): Consumer<Supplier<Boolean>> {
        return Consumer { dataSaverFunction: Supplier<Boolean> ->
            publication = super.dataProducer!!.get()
            var calledWithError = false
            var j = 0
            while (j < USER_RATIO && !super.finished && !calledWithError) {
                calledWithError = dataSaverFunction.get()
                j++
            }
        }
    }

    override fun handleDataForDataSaver(): PublicationDTO {
        publication.content = "";
        return publication
    }

    override val dTORepresentation: InserterDTO
        get() {
            val propertiesMap = HashMap<String, String>()
            propertiesMap.put("userRatio", Integer.toString(USER_RATIO))
            return InserterDTO(this.javaClass.simpleName,
                propertiesMap,
                "Inserts Publications. userRatio tells how many times to reuse one user before creating a new one."
            )
        }

    companion object {
        private const val USER_RATIO = 100
    }
}