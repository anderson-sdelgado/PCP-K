package br.com.usinasantafe.pcpk.features.external.room.database.stable

import br.com.usinasantafe.pcpk.features.external.room.dao.stable.TerceiroDao
import br.com.usinasantafe.pcpk.features.infra.datasource.room.stable.TerceiroDatasourceRoom
import br.com.usinasantafe.pcpk.features.infra.models.room.stable.TerceiroRoomModel
import javax.inject.Inject

class TerceiroDatasourceRoomImpl @Inject constructor (
    private val terceiroDao: TerceiroDao,
): TerceiroDatasourceRoom {

    override suspend fun addAllTerceiro(vararg terceiroRoomModels: TerceiroRoomModel) {
        terceiroDao.insertAll(*terceiroRoomModels)
    }

    override suspend fun checkCPFTerceiro(cpf: String): Boolean {
        return terceiroDao.checkCPFTerceiro(cpf) > 0
    }

    override suspend fun deleteAllTerceiro() {
        terceiroDao.deleteAll()
    }

    override suspend fun getTerceiroCPF(cpf: String): TerceiroRoomModel {
        return terceiroDao.getTerceiroCPF(cpf).first()
    }

    override suspend fun getTerceiroListCPF(cpf: String): List<TerceiroRoomModel> {
        return terceiroDao.getTerceiroCPF(cpf)
    }

    override suspend fun getTerceiroId(id: Long): TerceiroRoomModel {
        return terceiroDao.getTerceiroId(id).first()
    }

}