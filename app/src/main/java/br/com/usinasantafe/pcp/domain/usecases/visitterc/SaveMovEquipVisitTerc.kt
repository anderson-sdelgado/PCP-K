package br.com.usinasantafe.pcp.domain.usecases.visitterc

import br.com.usinasantafe.pcp.domain.entities.variable.MovEquipVisitTerc
import br.com.usinasantafe.pcp.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.pcp.domain.repositories.variable.MovEquipVisitTercPassagRepository
import br.com.usinasantafe.pcp.domain.repositories.variable.MovEquipVisitTercRepository
import javax.inject.Inject

interface SaveMovEquipVisitTerc {
    suspend operator fun invoke(): Boolean
    suspend operator fun invoke(movEquipVisitTerc: br.com.usinasantafe.pcp.domain.entities.variable.MovEquipVisitTerc): Boolean
}

class SaveMovEquipVisitTercImpl @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movEquipVisitTercRepository: MovEquipVisitTercRepository,
    private val movEquipVisitTercPassagRepository: MovEquipVisitTercPassagRepository,
): SaveMovEquipVisitTerc {
    override suspend fun invoke(): Boolean {
        val config = configRepository.getConfig()
        val idMov = movEquipVisitTercRepository.saveMovEquipVisitTerc(config.matricVigia!!, config.idLocal!!)
        if (idMov == 0L) return false
        if(!movEquipVisitTercPassagRepository.savePassag(idMov)) return false
        return true
    }

    override suspend fun invoke(movEquipVisitTerc: br.com.usinasantafe.pcp.domain.entities.variable.MovEquipVisitTerc): Boolean {
        val config = configRepository.getConfig()
        val idMov = movEquipVisitTercRepository.saveMovEquipVisitTerc(config.matricVigia!!, config.idLocal!!, movEquipVisitTerc)
        if (idMov == 0L) return false
        val passagList = movEquipVisitTercPassagRepository.listPassag(movEquipVisitTerc.idMovEquipVisitTerc!!)
        if(!movEquipVisitTercPassagRepository.savePassag(idMov, passagList)) return false
        return true
    }

}