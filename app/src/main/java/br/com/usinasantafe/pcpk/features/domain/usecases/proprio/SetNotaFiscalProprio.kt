package br.com.usinasantafe.pcpk.features.domain.usecases.proprio

import br.com.usinasantafe.pcpk.common.utils.FlowApp
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipProprioRepository
import javax.inject.Inject

interface SetNotaFiscalProprio {
    suspend operator fun invoke(notaFiscal: String, flowApp: FlowApp, pos: Int): Boolean
}

class SetNotaFiscalProprioImpl @Inject constructor(
    private val movEquipProprioRepository: MovEquipProprioRepository,
): SetNotaFiscalProprio {

    override suspend fun invoke(notaFiscal: String, flowApp: FlowApp, pos: Int): Boolean {
        return try {
            when(flowApp){
                FlowApp.ADD -> movEquipProprioRepository.setNotaFiscalMovEquipProprio(notaFiscal.toLong())
                FlowApp.CHANGE -> {
                    val movEquip = movEquipProprioRepository.listMovEquipProprioStarted()[pos]
                    movEquipProprioRepository.updateNotaFiscalMovEquipProprio(notaFiscal.toLong(), movEquip)
                }
            }
        } catch (exception: Exception) {
            false
        }
    }

}