package br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.visitterc

import br.com.usinasantafe.pcpk.common.utils.TypeAddOcupante

interface SetCpfMotoristaPassagVisitTerc {

    suspend operator fun invoke(cpf: String, typeAddOcupante: TypeAddOcupante, pos: Int): Boolean

}