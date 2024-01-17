package br.com.usinasantafe.pcpk.features.domain.usecases.implementations.visitterc

import br.com.usinasantafe.pcpk.features.domain.entities.variable.MovEquipVisitTerc
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipVisitTercPassagRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipVisitTercRepository
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.common.StartProcessSendData
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.visitterc.SaveMovEquipVisitTerc
import javax.inject.Inject

class SaveMovEquipVisitTercImpl @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movEquipVisitTercRepository: MovEquipVisitTercRepository,
    private val movEquipVisitTercPassagRepository: MovEquipVisitTercPassagRepository,
): SaveMovEquipVisitTerc {
    override suspend fun invoke(): Boolean {
        val config = configRepository.getConfig()
        val idMov = movEquipVisitTercRepository.saveMovEquipVisitTerc(config.matricVigia!!, config.idLocal!!)
        if(idMov == 0) return false
        if(!movEquipVisitTercPassagRepository.savePassag(idMov)) return false
        return true
    }

    override suspend fun invoke(movEquipVisitTerc: MovEquipVisitTerc): Boolean {
        val config = configRepository.getConfig()
        val idMov = movEquipVisitTercRepository.saveMovEquipVisitTerc(config.matricVigia!!, config.idLocal!!, movEquipVisitTerc)
        if(idMov == 0) return false
        val passagList = movEquipVisitTercPassagRepository.listPassagIdMov(movEquipVisitTerc.idMovEquipVisitTerc!!)
        if(!movEquipVisitTercPassagRepository.savePassag(idMov, passagList)) return false
        return true
    }

}