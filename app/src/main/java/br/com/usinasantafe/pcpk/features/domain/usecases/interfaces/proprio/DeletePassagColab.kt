package br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.proprio

import br.com.usinasantafe.pcpk.common.utils.TypeAddOcupante

interface DeletePassagColab {

    suspend operator fun invoke(posList: Int, typeAddOcupante: TypeAddOcupante, pos: Int): Boolean

}