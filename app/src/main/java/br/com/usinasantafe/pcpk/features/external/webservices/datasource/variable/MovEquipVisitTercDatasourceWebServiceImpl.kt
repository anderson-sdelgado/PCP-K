package br.com.usinasantafe.pcpk.features.external.webservices.datasource.variable

import br.com.usinasantafe.pcpk.common.utils.token
import br.com.usinasantafe.pcpk.features.external.webservices.api.variable.MovEquipVisitTercApi
import br.com.usinasantafe.pcpk.features.infra.datasource.webservice.variable.MovEquipVisitTercDatasourceWebService
import br.com.usinasantafe.pcpk.features.infra.models.webservice.MovEquipVisitTercWebServiceModelInput
import br.com.usinasantafe.pcpk.features.infra.models.webservice.MovEquipVisitTercWebServiceModelOutput
import javax.inject.Inject

class MovEquipVisitTercDatasourceWebServiceImpl @Inject constructor(
    private val movEquipVisitTercApi: MovEquipVisitTercApi,
): MovEquipVisitTercDatasourceWebService {
    override suspend fun sendMovEquipVisitTerc(
        movEquipVisitTercList: List<MovEquipVisitTercWebServiceModelOutput>,
        nroAparelho: Long
    ): Result<List<MovEquipVisitTercWebServiceModelInput>> {
        val response = movEquipVisitTercApi.send(token(nroAparelho), movEquipVisitTercList)
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Throwable("Erro envio de dados"))
        }
    }
}