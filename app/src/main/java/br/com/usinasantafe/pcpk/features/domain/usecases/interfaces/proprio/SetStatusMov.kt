package br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.proprio

import br.com.usinasantafe.pcpk.common.utils.StatusMovEquipProprio

interface SetStatusMov {

    suspend operator fun invoke(statusMovEquipProprio: StatusMovEquipProprio): Boolean

}