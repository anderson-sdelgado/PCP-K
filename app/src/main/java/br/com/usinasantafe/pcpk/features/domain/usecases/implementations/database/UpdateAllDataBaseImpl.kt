package br.com.usinasantafe.pcpk.features.domain.usecases.implementations.database

import br.com.usinasantafe.pcpk.common.utils.FlagUpdate
import br.com.usinasantafe.pcpk.common.utils.TEXT_SUCESS_UPDATE
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.UpdateAllDataBase
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateColab
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateEquip
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateLocal
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateTerceiro
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateVisitante
import br.com.usinasantafe.pcpk.common.utils.ResultUpdateDatabase
import br.com.usinasantafe.pcpk.common.utils.TEXT_FINISH_CONFIG
import br.com.usinasantafe.pcpk.common.utils.TEXT_SAVE_DATA_CONFIG
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.initial.RecoverConfig
import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.initial.SetCheckUpdateBDConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
class UpdateAllDataBaseImpl @Inject constructor(
    private val updateColab: UpdateColab,
    private val updateEquip: UpdateEquip,
    private val updateLocal: UpdateLocal,
    private val updateTerceiro: UpdateTerceiro,
    private val updateVisitante: UpdateVisitante,
    private val setCheckUpdateBDConfig: SetCheckUpdateBDConfig,
) : UpdateAllDataBase {

    override suspend fun invoke(count: Int, size: Int): Flow<Result<ResultUpdateDatabase>> = flow {
        val size = size
        var count = count
        updateColab(count, size).collect { result ->
            result.fold(
                onSuccess = {
                    emit(Result.success(it))
                    count = it.count;
                },
                onFailure = { exception -> emit(Result.failure(exception)) }
            )
        }
        updateEquip(count, size).collect { result ->
            result.fold(
                onSuccess = {
                    emit(Result.success(it))
                    count = it.count;
                },
                onFailure = { exception -> emit(Result.failure(exception)) }
            )
        }
        updateLocal(count, size).collect { result ->
            result.fold(
                onSuccess = {
                    emit(Result.success(it))
                    count = it.count;
                },
                onFailure = { exception -> emit(Result.failure(exception)) }
            )
        }
        updateTerceiro(count, size).collect { result ->
            result.fold(
                onSuccess = {
                    emit(Result.success(it))
                    count = it.count;
                },
                onFailure = { exception -> emit(Result.failure(exception)) }
            )
        }
        updateVisitante(count, size).collect { result ->
            result.fold(
                onSuccess = {
                    emit(Result.success(it))
                    count = it.count;
                },
                onFailure = { exception -> emit(Result.failure(exception)) }
            )
        }
        setCheckUpdateBDConfig(FlagUpdate.UPDATED)
        emit(Result.success(ResultUpdateDatabase(size, TEXT_SUCESS_UPDATE, size)))
    }

}