package br.com.usinasantafe.pcpk.features.domain.usecases.implementations.proprio

import br.com.usinasantafe.pcpk.common.utils.TypeAddEquip
import br.com.usinasantafe.pcpk.features.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipProprioRepository
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.MovEquipProprioSegRepository
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.proprio.RecoverListEquipProprioSeg
import javax.inject.Inject

class RecoverListEquipProprioSegImpl @Inject constructor(
    private val movEquipProprioRepository: MovEquipProprioRepository,
    private val movEquipProprioSegRepository: MovEquipProprioSegRepository,
    private val equipRepository: EquipRepository,
) : RecoverListEquipProprioSeg {

    override suspend fun invoke(typeAddEquip: TypeAddEquip, pos: Int): List<String> {
        return when (typeAddEquip) {
            TypeAddEquip.ADDVEICULO,
            TypeAddEquip.ADDVEICULOSEG -> movEquipProprioSegRepository.listEquipSeg()
                .map { equipRepository.getEquipId(it.idEquipMovEquipProprioSeg!!).nroEquip.toString() }
            TypeAddEquip.CHANGEVEICULO,
            TypeAddEquip.CHANGEVEICULOSEG -> {
                val movEquip = movEquipProprioRepository.listMovEquipProprioEmpty()[pos]
                movEquipProprioSegRepository.listEquipSeg(movEquip.idMovEquipProprio!!)
                    .map { equipRepository.getEquipId(it.idEquipMovEquipProprioSeg!!).nroEquip.toString() }
            }
        }
    }

}