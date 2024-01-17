package br.com.usinasantafe.pcpk.features.domain.usecases.implementations.common

import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipProprioRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipResidenciaRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipVisitTercRepository
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.common.SetStatusSendCloseAllMov
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.common.StartProcessSendData
import javax.inject.Inject

class SetStatusSendCloseAllMovImpl @Inject constructor(
    private val movEquipProprioRepository: MovEquipProprioRepository,
    private val movEquipVisitTercRepository: MovEquipVisitTercRepository,
    private val movEquipResidenciaRepository: MovEquipResidenciaRepository,
    private val startProcessSendData: StartProcessSendData,
): SetStatusSendCloseAllMov {

    override suspend fun invoke(): Boolean {
        try{
            val movEquipProprioList = movEquipProprioRepository.listMovEquipProprioEmpty()
            for (movEquipProprio in movEquipProprioList) {
                movEquipProprioRepository.setStatusSendClosedMov(movEquipProprio)
            }
            val movEquipVisitTercList = movEquipVisitTercRepository.listMovEquipVisitTercEmpty()
            for (movEquipVisitTerc in movEquipVisitTercList) {
                movEquipVisitTercRepository.setStatusSendClosedMov(movEquipVisitTerc)
            }
            val movEquipResidenciaList = movEquipResidenciaRepository.listMovEquipResidenciaEmpty()
            for (movEquipResidencia in movEquipResidenciaList) {
                movEquipResidenciaRepository.setStatusSendClosedMov(movEquipResidencia)
            }
            startProcessSendData()
        } catch (exception: Exception) {
            return false
        }
        return true
    }

}